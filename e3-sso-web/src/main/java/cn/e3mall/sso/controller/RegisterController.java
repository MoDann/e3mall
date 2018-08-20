package cn.e3mall.sso.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.RegisterService;

/**
 * 注册功能Controller
 * @author ASUS
 *
 */
@Controller
public class RegisterController {

	@Autowired
	private RegisterService registerService;
	
	@RequestMapping("/page/register")
	public String showRegister() {
		return "register";
	}
	
	@RequestMapping("/user/check/{param}/{type}")
	@ResponseBody
	public E3mallResult checkData(@PathVariable String param, @PathVariable Integer type) {
		E3mallResult result = registerService.checkData(param, type);
		return result;
	}
	
	@RequestMapping(value="/user/register",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult register(TbUser user) {
		E3mallResult result = registerService.register(user);
		return result;
	}
}
