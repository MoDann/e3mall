package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.CookieUtils;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.sso.service.LoginService;

/**
 * 登录功能Controller
 * @author ASUS
 *
 */
@Controller
public class LoginController {

	@Autowired
	private LoginService loginService;
	
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLogin(String redirect,Model model) {
		//判断哪里发出的请求，登录成功后需要跳回请求发起的地方
		model.addAttribute("redirect", redirect);
		return "login";
	}
	
	@RequestMapping(value="/user/login",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult userLogin(String username, String password, 
			HttpServletRequest request, HttpServletResponse response) {
		E3mallResult result = loginService.userLogin(username, password);
		//判断是否登录成功
		if (result.getStatus() == 200) {
			String token = result.getData().toString();
			//把token写进cookie中
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		return result;
	}
	
	
}
