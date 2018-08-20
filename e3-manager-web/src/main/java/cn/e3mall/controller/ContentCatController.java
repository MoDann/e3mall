package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.content.service.ContentCategoryService;

/*
 * 商品内容分类管理
 */
@Controller
public class ContentCatController {
	
	@Autowired
	private ContentCategoryService contentCategoryService;
	
	/*
	 * 查询内容分类管理节点
	 */
	@RequestMapping("/content/category/list")
	@ResponseBody
	public List<EasyUITreeNode> getContentCatList(
			@RequestParam(value="id", defaultValue="0") Long parentId){
		
			List<EasyUITreeNode> categoryList = contentCategoryService.getContentCategoryList(parentId);
			return categoryList;
		
	}
	
	/*
	 * 添加内容分类管理节点
	 */
	@RequestMapping(value="/content/category/create",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult addItemContentCatagory(long parentId,String name) {
		
		E3mallResult result = contentCategoryService.addContentCategory(parentId, name);
		return result;
	}
	
	/*
	 * 重命名节点
	 */
	@RequestMapping("/content/category/update")
	@ResponseBody
	public E3mallResult updateContentCategory(long id,String name) {
		
		E3mallResult result = contentCategoryService.updateContentCategory(id, name);
		return result;
	}
	
	/*
	 * 删除节点
	 */
	@RequestMapping("/content/category/delete/")
	@ResponseBody
	public E3mallResult deleteContentCategory(long id) {
		
		E3mallResult result = contentCategoryService.deleteContentCategory(id);
		return result;
	}
}
