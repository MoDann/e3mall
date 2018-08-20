package cn.e3mall.cart.service;


import java.util.List;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;

/**
 * 购物车管理Service
 * @author ASUS
 *
 */
public interface CartService {

	E3mallResult addCart(long userId,long itemId,int num);
	//合并购物车
	E3mallResult mergeCart(long userId, List<TbItem> itemList);
	//获取购物车列表
	List<TbItem> getCartList(long userId);
	//修改购物车
	E3mallResult updateCartItemNum(long userId, long itemId, int num);
	//删除购物车信息
	E3mallResult deleteCartItem(long userId, long itemId);
	//清空购物车
	E3mallResult clearCart(long userId);
	//删除选中的购物车商品
	E3mallResult cartDelMore(long userId,String[] ids);
}
