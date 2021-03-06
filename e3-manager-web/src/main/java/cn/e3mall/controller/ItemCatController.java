package cn.e3mall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.service.ItemCatService;

/*
 * 商品分类管理Controller
 */
@Controller
public class ItemCatController {
	
	@Autowired
	private ItemCatService itemCatService;
	
	@RequestMapping("/item/cat/list")
	@ResponseBody
	//请求的参数为id，且第一次请求没有id
	public List<EasyUITreeNode> getItemCatList(
			@RequestParam(value="id",defaultValue="0")long parentId) {
		List<EasyUITreeNode> catList = itemCatService.getItemCatList(parentId);
		return catList;
	}

}
