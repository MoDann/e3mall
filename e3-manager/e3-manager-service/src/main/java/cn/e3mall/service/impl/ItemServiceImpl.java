package cn.e3mall.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.jedis.JedisClientPool;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.IDUtils;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemDescMapper;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.mapper.TbItemParamItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemExample;
import cn.e3mall.pojo.TbItemExample.Criteria;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.service.ItemService;
import redis.clients.jedis.JedisPool;

/*
 * 商品管理Service
 */

@Service
public class ItemServiceImpl implements ItemService {

	@Autowired
	 private TbItemMapper itemMapper;
	
	@Autowired
	private TbItemDescMapper itemDescMapper;
	
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	
	@Autowired
	private JmsTemplate jmsTemplate;
	//注入消息类型
	@Resource
	private Destination topicDestination;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_ITEM_PRE}")
	private String REDIS_ITEM_PRE;
	
	@Value("${ITEM_CACHE_EXPIRE}")
	private Integer ITEM_CACHE_EXPIRE;
	
	@Override
	public TbItem getItemById(long itemId) {
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
			if (!StringUtils.isBlank(json)) {
				TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
				return tbItem;
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		//缓存中没有，查询数据库
		//根据id获取商品信息
//		TbItem item = itemMapper.selectByPrimaryKey(itemId);

		//根据条件查询
		TbItemExample example = new TbItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemId);
		List<TbItem> list = itemMapper.selectByExample(example);
		
		if (list!=null && list.size()>0) {
			//添加到缓存
			try {
				jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(list.get(0)));
				//设置缓存过期时间
				jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return list.get(0);
		}
		return null;
//		return item;
	}
	
	@Override
	public EasyUIDataGridResult getItemList(int page, int rows) {
		
		//设置分页信息
		PageHelper.startPage(page, rows);
		//执行查询
		TbItemExample example = new TbItemExample();
		List<TbItem> list = itemMapper.selectByExample(example);
		//创建一个返回对象
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//获取分页信息
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		long total = pageInfo.getTotal();
		result.setTotal(total);

		return result;
	}

	@Override
	public E3mallResult addItem(TbItem item, String desc,TbItemParamItem itemParamItem) {
	
		//生成商品id（根据工具类）
		final long itemId = IDUtils.genItemId();
		//补全商品属性
		item.setId(itemId);
		//1-正常，2-下架，3-删除
		item.setStatus((byte) 1);
		item.setCreated(new Date());
		item.setUpdated(new Date());
		//将商品插入商品表
		itemMapper.insert(item);
		//创建一个商品描述信息的pojo（实体、bean）对象
		TbItemDesc tbItemDesc = new TbItemDesc();
		//补全属性
		tbItemDesc.setItemId(itemId);
		tbItemDesc.setItemDesc(desc);
		tbItemDesc.setCreated(new Date());
		tbItemDesc.setUpdated(new Date());
		//插入商品描述表
		itemDescMapper.insert(tbItemDesc);
		
		//补全商品规格属性
		itemParamItem.setItemId(itemId);
		itemParamItem.setCreated(new Date());
		itemParamItem.setUpdated(new Date());
		//插入商品规格表
		itemParamItemMapper.insert(itemParamItem);
		
		//发送商品添加消息
		jmsTemplate.send(topicDestination, new MessageCreator() {
			
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage textMessage = session.createTextMessage(itemId + "");
				return textMessage;
			}
		});
		
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult getItemDesc(long itemId) {

		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		return E3mallResult.ok(itemDesc);
	}
	
	@Override
	public E3mallResult getItemParamItem(long itemId) {

		TbItemParamItem itemParamItem = itemParamItemMapper.selectByPrimaryKey(itemId);
		return E3mallResult.ok(itemParamItem);
	}
	
	
	@Override
	public E3mallResult updateItem(TbItem item, String desc,String paramData) {

		Long itemId = item.getId();
		//根据商品id获取商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//执行修改条件
		item.setCreated(tbItem.getCreated());
		item.setUpdated(new Date());
		//保存修改信息
		//itemMapper.updateByPrimaryKey(item);
		itemMapper.updateByPrimaryKeySelective(item);
		//缓存同步
		String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
		if (!StringUtils.isBlank(json)) {
			jedisClient.hdel(json, null);
		}
		
		//执行描述信息条件
		TbItemDesc tbItemDesc = new TbItemDesc();
		tbItemDesc.setItemId(itemId);
	    tbItemDesc.setItemDesc(desc);
	    tbItemDesc.setUpdated(new Date());
	    tbItemDesc.setCreated(itemDescMapper.selectByPrimaryKey(itemId).getCreated());
		//保存描述信息修改
		itemDescMapper.updateByPrimaryKey(tbItemDesc);
		String string = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
		if (!StringUtils.isBlank(string)) {
			jedisClient.hdel(string, null);
		}
		
		//修改商品规格信息
		TbItemParamItem itemParamItem = itemParamItemMapper.selectByPrimaryKey(itemId);
		if (itemParamItem!=null) {
			TbItemParamItem paramItem = new TbItemParamItem();
			paramItem.setItemId(itemId);
			paramItem.setParamData(paramData);
			paramItem.setUpdated(new Date());
			Date created = itemParamItem.getCreated();
			System.out.println(created);
			paramItem.setCreated(created);
			//保存
			itemParamItemMapper.updateByPrimaryKeySelective(paramItem);	
		}
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteItem(String ids) {

		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				//删除商品
				itemMapper.deleteByPrimaryKey(Long.valueOf(id));
				//删除商品描述信息
				itemDescMapper.deleteByPrimaryKey(Long.valueOf(id));
				//删除商品规格
				itemParamItemMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
			return E3mallResult.ok();
		}
		return null;
	}

	//商品下架
	@Override
	public E3mallResult instockItem(String ids) {

		//商品id不能为空
		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(id));
				//1-正常，2-下架，3-删除
				item.setStatus((byte) 2);
				
				itemMapper.updateByPrimaryKey(item);
			}
			return E3mallResult.ok();
		}
		return null;
	}

	@Override
	public E3mallResult reshelfItem(String ids) {

		//商品id不能为空
		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				TbItem item = itemMapper.selectByPrimaryKey(Long.valueOf(id));
				//1-正常，2-下架，3-删除
				item.setStatus((byte) 1);
				
				itemMapper.updateByPrimaryKey(item);
			}
			return E3mallResult.ok();
		}		
		return null;
	}

	@Override
	public TbItemDesc getItemDescById(long itemId) {
		
		//查询缓存
		try {
			String json = jedisClient.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
			if (!StringUtils.isBlank(json)) {
				TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return tbItemDesc;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = itemDescMapper.selectByPrimaryKey(itemId);
		//添加到缓存
		try {
			jedisClient.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", JsonUtils.objectToJson(itemDesc));
			//设置缓存过期时间
			jedisClient.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

}
