package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.content.service.ContentService;
import cn.e3mall.pojo.TbContent;

/*
 * 商品内容管理Controller
 */
@Controller
public class ContentController {

	@Autowired
	private ContentService contentService;
	
	@RequestMapping("/content/query/list")
	@ResponseBody
	public EasyUIDataGridResult getContent(long categoryId,int page,int rows) {
		
		EasyUIDataGridResult result = contentService.getContent(categoryId, page, rows);
		return result;
	}
	
	@RequestMapping("/content/save")
	@ResponseBody
	public E3mallResult addContent(TbContent content) {
		
		E3mallResult result = contentService.addContent(content);
		return result;
	}
	
	@RequestMapping("/rest/content/edit")
	@ResponseBody
	public E3mallResult updateContent(TbContent content) {
		
		E3mallResult result = contentService.updateConten(content);
		return result;
	}
	
	@RequestMapping("/content/delete")
	@ResponseBody
	public E3mallResult deleteContent(String ids) {
		
		E3mallResult result = contentService.deleteContent(ids);
		return result;
	}
	
}
