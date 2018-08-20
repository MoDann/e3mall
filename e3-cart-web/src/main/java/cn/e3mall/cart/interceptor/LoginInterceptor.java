package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
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
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		/*
		 * 在方法handler执行之前执行
		 * 返回true：放行    false：拦截
		 */
		//1.从cookie中取token
		String token = CookieUtils.getCookieValue(request, "token");
		//2.如果没有token，用户未登录，直接放行
		if (StringUtils.isBlank(token)) {
			return true;
		}
		//3.取到token，调用sso服务，从token中取用户信息
		E3mallResult result = tokenService.getUserByToken(token);
		//4.没有取到用户信息，用户登录已过期，直接放行
		if (result.getStatus() != 200) {
			return true;
		}
		//5.取到用户信息，用户已登录
		TbUser user = (TbUser) result.getData();
		//6.把用户信息放到request中，只需要在Controller中判断request中是否含有user。放行
		request.setAttribute("user", user);
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
