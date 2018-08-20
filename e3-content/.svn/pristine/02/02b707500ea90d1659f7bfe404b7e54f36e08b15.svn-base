package cn.e3mall.content.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.mapper.TbContentMapper;
import cn.e3mall.pojo.TbContent;
import cn.e3mall.pojo.TbContentExample;
import cn.e3mall.pojo.TbContentExample.Criteria;

/*
 * 商品分类管理显示
 */
@Service
public class ContentServiceImpl implements ContentService {

	//缓存中商品分类存储的key
	@Value("${CONTENT_LIST}")
	private String CONTENT_LIST;
	
	@Autowired
	private TbContentMapper contentMapper;
	
	@Autowired
	private JedisClient jedisClient;
	
	@Override
	public EasyUIDataGridResult getContent(long categoryId, int page, int rows) {

		//根据categoryId查询
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(categoryId);
		//执行查询
		List<TbContent> list = contentMapper.selectByExample(example);
		
		//分页
		PageHelper.startPage(page, rows);
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(list);
		//转化为pageInfo对象
		PageInfo<TbContent> pageInfo = new PageInfo<>(list);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}

	@Override
	public E3mallResult addContent(TbContent content) {

		//补全属性
		content.setCreated(new Date());
		content.setUpdated(new Date());
		//插入数据
		contentMapper.insert(content);
		//插入时实现缓存同步（删除缓存中对应的）
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3mallResult.ok();
		
	}

	@Override
	public E3mallResult updateConten(TbContent content) {

		//根据id获取内容
		Long id = content.getId();
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(id);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		for (TbContent tbContent : list) {
			content.setCreated(tbContent.getCreated());
		}
		content.setUpdated(new Date());
		//保存修改信息
		contentMapper.updateByPrimaryKeySelective(content);
		//修改时实现缓存同步
		jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteContent(String ids) {

		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				//根据id查询对应的商品管理对象
				TbContent content = contentMapper.selectByPrimaryKey(Long.valueOf(id));
				//删除时实现缓存同步
				jedisClient.hdel(CONTENT_LIST, content.getCategoryId().toString());
				//删除商品管理
				contentMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
			return E3mallResult.ok();
		}
		return null;
	}

	/*
	 * 根据CategoryId查询商品内容（实现首页轮播）
	 */
	@Override
	public List<TbContent> getContentByCid(long cid) {

		//先在缓存中查询
		//防止异常
		try {
			String hget = jedisClient.hget(CONTENT_LIST, cid+"");
			if (StringUtils.isNotEmpty(hget)) {
				//转换为list
				List<TbContent> list = JsonUtils.jsonToList(hget, TbContent.class);
				return list;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//如果缓存中没有再查询数据库
		//设置查询条件
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		//执行查询
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		//将结果放入缓存
		try {
			jedisClient.hset(CONTENT_LIST, cid + "", JsonUtils.objectToJson(list));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

}
