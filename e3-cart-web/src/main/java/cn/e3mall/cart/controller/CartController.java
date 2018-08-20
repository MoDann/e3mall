package cn.e3mall.cart.controller;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.service.ItemService;

/**
 * 购物车管理Controller
 * @author ASUS
 *
 */
@Controller
public class CartController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private CartService cartService;
	
	@Value("${COOKIE_CART_EXPIRE}")
	private Integer COOKIE_CART_EXPIRE;
	
	/**
	 * 添加商品到购物车（未登陆）
	 * @param itemId
	 * @param num
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("/cart/add/{itemId}")
	public String addCart(@PathVariable Long itemId,@RequestParam(defaultValue="1") Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		
		//判断用户是否登录
		TbUser user = (TbUser) request.getAttribute("user");
		//如果登录，写入redis
		if (user != null) {
			//保存购物车信息到redis
			cartService.addCart(user.getId(), itemId, num);
			//返回逻辑视图
			return "cartSuccess";
		}
		//如果未登录，写入cookie
		//从cookie中获取购物车列表
		List<TbItem> cartList = getCartFromCookie(request);
		//判断商品是否在列表中存在
		boolean flag = false;
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId.longValue()) {
				flag = true;
				//存在商品数量相加
				tbItem.setNum(tbItem.getNum() + num);
				//跳出循环
				break;
			}
		}
		//如果不存在，就根据商品查询商品信息，封装为一个TbItem对象
		if (!flag) {
			TbItem tbItem = itemService.getItemById(itemId);
			//设置商品数量
			tbItem.setNum(num);
			//取第一张图片
			String image = tbItem.getImage();
			if (StringUtils.isNotBlank(image)) {
				String[] images = image.split(",");
				tbItem.setImage(images[0]);
			}
			//添加到商品列表
			cartList.add(tbItem);
		}
		//添加到Cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回成功页面
		return "cartSuccess";
	}
	
	/**
	 * 从Cookie中取购物车列表
	 * @param request
	 * @return
	 */
	public List<TbItem> getCartFromCookie(HttpServletRequest request) {
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isBlank(json)) {
			//为空返回空的列表
			return new ArrayList<TbItem>();
		}
		List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);
		return list;
	}
	
	/**
	 * 展示购物车信息
	 * @param request
	 * @return
	 */
	@RequestMapping("/cart/cart")
	public String showCart(HttpServletRequest request,HttpServletResponse response) {
		//从Cookie中获取购物车信息
		List<TbItem> cartList = getCartFromCookie(request);
		//获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//用户登录
		if (user != null) {
			if (!cartList.isEmpty()) {
				//合并购物车
				cartService.mergeCart(user.getId(), cartList);
				//删除cookie中的购物车
				CookieUtils.setCookie(request, response, "cart", "");
			}
			//从redis中获取用户购物车信息
			//从服务端取购物车列表
			List<TbItem> list = cartService.getCartList(user.getId());
			request.setAttribute("cartList", list);
			return "cart";
		}
		//把列表传递给页面
		request.setAttribute("cartList", cartList);
		return "cart";
	}
	
	/**
	 * 更新购物车商品数量
	 * @param itemId
	 * @param num
	 * @return
	 */
	@RequestMapping("/cart/update/num/{itemId}/{num}")
	@ResponseBody
	public E3mallResult updateCartNum(@PathVariable Long itemId,@PathVariable Integer num,
			HttpServletRequest request,HttpServletResponse response) {
		//取用户登录信息
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.updateCartItemNum(user.getId(), itemId, num);
			return E3mallResult.ok();
		}
		
		//如果未登录
		//从Cookie中获取购物车列表
		List<TbItem> cartList = getCartFromCookie(request);
		//遍历获取商品信息
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//更新商品数量
				tbItem.setNum(num);
				break;
			}
		}
		//将结果写回Cookie中
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//返回结果
		return E3mallResult.ok();
	}
	
	@RequestMapping("/cart/delete/{itemId}")
	public String deleteCartItem(@PathVariable Long itemId,HttpServletRequest request,
			HttpServletResponse response) {
		//取用户登录信息
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.deleteCartItem(user.getId(), itemId);
			return "redirect:/cart/cart.html";
		}
		
		//未登录
		//从Cookie中获取购物车列表
		List<TbItem> cartList = getCartFromCookie(request);
		//遍历列表，删除需要删除的商品
		for (TbItem tbItem : cartList) {
			if (tbItem.getId().longValue() == itemId) {
				//删除
				cartList.remove(tbItem);
				break;
			}
		}
		//写回给Cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		//重定向
		return "redirect:/cart/cart.html";
	}
	
	/**
	 * 清空购物车
	 * @param request
	 * @return
	 */
	
	@RequestMapping("/cart/clearCart")
	public String clearCart(HttpServletRequest request,HttpServletResponse response) {
		//获取用户登录信息
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			//清空redis
			cartService.clearCart(user.getId());
			return "redirect:/cart/cart.html";
		}
		//清空Cookie
		CookieUtils.deleteCookie(request, response, "cart");
		return "redirect:/cart/cart.html";
	}
	
	/**
	 * 删除选中的商品信息
	 * @param ids
	 * @param request
	 * @param response
	 * @return
	 */
	//localhost:8092/cart/cartDelMore
	@RequestMapping("/cart/cartDelMore")
	@ResponseBody
	public E3mallResult cartDelMore(String[] ids,HttpServletRequest request,HttpServletResponse response) {
		//获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		if (user != null) {
			cartService.cartDelMore(user.getId(), ids);
			return E3mallResult.ok();
		}
		
		//未登录
		//从Cookie中获取购物车列表
		List<TbItem> cartList = getCartFromCookie(request);
		//遍历删除对应的商品
		for(String id:ids) {
			for (TbItem tbItem : cartList) {
				if (Long.parseLong(id) == tbItem.getId().doubleValue()) {
					cartList.remove(tbItem);
					break;
				}
			}
		}
		//重新写回给Cookie
		CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), COOKIE_CART_EXPIRE, true);
		return E3mallResult.ok();
	}
	
}
