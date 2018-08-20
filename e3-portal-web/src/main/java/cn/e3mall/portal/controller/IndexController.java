package cn.e3mall.portal.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/*
 * 商品前台首页
 */
@Controller
public class IndexController {
	
	@Autowired
	private ContentService contentService;
	
	@Value("${CONTENT_LUNBO_ID}")
	private long CONTENT_LUNBO_ID;
	
	//通过拦截*.html访问
	@RequestMapping("/index")
	public String showIndex(Model model) {
		
		//获取列表
		List<TbContent> ad1List = contentService.getContentByCid(CONTENT_LUNBO_ID);
		//把结果传递给页面
		model.addAttribute("ad1List", ad1List);
		return "index";
	}

}
