package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.search.service.SearchItemService;

/*
 *  导入商品数据到索引库Controller
 */
@Controller
public class SearchItemController {

	@Autowired
	private SearchItemService searchItemService;
	
	@RequestMapping("/index/item/import")
	@ResponseBody
	public E3mallResult importAllItems() {
		
		E3mallResult result = searchItemService.importAllItems();
		return result;
	}
}
