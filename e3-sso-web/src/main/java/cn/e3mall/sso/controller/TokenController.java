package cn.e3mall.sso.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.common.util.JsonUtils;
import cn.e3mall.sso.service.TokenService;

/**
 *  获取TokenController
 * @author ASUS
 *
 */
@Controller
public class TokenController {

	@Autowired
	private TokenService tokenService;
	
/*	@RequestMapping(value="/user/token/{token}",produces=MediaType.APPLICATION_JSON_UTF8_VALUE"application/json;charset=utf-8")
	@ResponseBody
	public String getUserByToken(@PathVariable String token, String callback) {
		E3mallResult result = tokenService.getUserByToken(token);
		//响应结果之前，先判断是否为Jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			//把结果封装为一个js语句
			return callback + "(" + JsonUtils.objectToJson(result) + ");";
		}
		return JsonUtils.objectToJson(result);
	}*/
	
	@RequestMapping(value="/user/token/{token}")
	@ResponseBody
	public Object getUserByToken(@PathVariable String token, String callback) {
		E3mallResult result = tokenService.getUserByToken(token);
		//响应结果之前，先判断是否为Jsonp请求
		if (StringUtils.isNotBlank(callback)) {
			//把结果封装为一个js语句
			//Spring4.1以后支持
			MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
			mappingJacksonValue.setJsonpFunction(callback);
			return mappingJacksonValue;
		}
		return result;
	}
}
