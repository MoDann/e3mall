package cn.e3mall.order.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;


/**
 *    判断用户是否登录拦截器
 * @author ASUS
 *
 */

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CartService cartService;
	
	@Value("${SSO_URL}")
	private String SSO_URL;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 * 在方法handler执行之前执行
		 * 返回true：放行    false：拦截
		 */
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2.如果没有token，用户未登录，拦截，跳转到sso登录页面，登录成功后需要跳回来
		if (StringUtils.isBlank(token)) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//3.取到token，调用sso服务，从token中取用户信息
		E3mallResult result = tokenService.getUserByToken(token);
		//4.没有取到用户信息，用户登录已过期，拦截，跳转到sso登录页面，登录成功后需要跳回来
		if (result.getStatus() != 200) {
			response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
			return false;
		}
		//5.取到用户信息，用户已登录
		TbUser user = (TbUser) result.getData();
		//6.把用户信息放到request中
		request.setAttribute("user", user);
		//判断cookie中是否有购物车数据，有就合并
		String json = CookieUtils.getCookieValue(request, "cart", true);
		if (StringUtils.isNotBlank(json)) {
			cartService.mergeCart(user.getId(), JsonUtils.jsonToList(json, TbItem.class));
		}
		//放行
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		/*
		 * 在方法Handler方法执行之后，返回ModelAndView之前执行
		 */
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		/*
		 * 返回逻辑视图之后执行，此时已经跳转
		 * 可以在此处理异常信息
		 */
		
	}

}
