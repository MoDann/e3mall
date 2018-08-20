package cn.e3mall.cart.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;

/**
 * 购物车管理Service
 * @author ASUS
 *
 */
@Service
public class CartServiceImpl implements CartService {

	@Autowired
	private JedisClient jedisClient;
	
	@Value("${REDIS_CART_PRE}")
	private String REDIS_CART_PRE;
	
	@Autowired
	private TbItemMapper itemMapper;
	
	@Override
	public E3mallResult addCart(long userId, long itemId, int num) {
		/*
		 * 向redis中添加购物车信息
		 * 使用hset存储，key：用户id，field：商品Id，value：商品信息
		 */
		//判断商品是否存在
		Boolean hexists = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
		//存在就数量相加
		if (hexists) {
			//从redis中获取到购物车信息
			String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
			//将json转化为商品列表
			TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
			//商品数量相加
			item.setNum(item.getNum() + num);
			//写回给redis
			jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
			//返回
			return E3mallResult.ok();
		}
		//不存在就根据商品id查询商品信息
		TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);
		//设置商品数量
		tbItem.setNum(num);
		//取第一张图片
		String image = tbItem.getImage();
		if (StringUtils.isNotBlank(image)) {
			tbItem.setImage(image.split(",")[0]);
		}
		//写回给redis
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
		return E3mallResult.ok();
	}

	/**
	 * 合并购物车信息
	 */
	@Override
	public E3mallResult mergeCart(long userId, List<TbItem> itemList) {
		//遍历商品列表
		for (TbItem tbItem : itemList) {
			//添加商品到购物车
			addCart(userId, tbItem.getId(), tbItem.getNum());
		}
		return E3mallResult.ok();
	}

	@Override
	public List<TbItem> getCartList(long userId) {
		//从redis中获取购物车列表
		//hvals根据用户Id取值（商品信息）
		List<String> strings = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
		//把json列表转化为item列表
		List<TbItem> items = new ArrayList<>();
		for (String string : strings) {
			TbItem item = JsonUtils.jsonToPojo(string, TbItem.class);
			items.add(item);
		}
		return items;
	}

	@Override
	public E3mallResult updateCartItemNum(long userId, long itemId, int num) {

		//从redis中获取购物车列表
		String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
		//转化为item对象
		TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
		//设置数量
		item.setNum(num);
		//写回给redis
		jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(item));
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteCartItem(long userId, long itemId) {
		//从redis中删除对应的商品信息
		jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult clearCart(long userId) {
		//删除redis中用户Id对应的缓存
		jedisClient.del(REDIS_CART_PRE + ":" +userId); 
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult cartDelMore(long userId, String[] ids) {
		//遍历ids
		for (String id: ids) {
			jedisClient.hdel(REDIS_CART_PRE + ":" + userId, id + "");
		}
		return E3mallResult.ok();
	}

}
