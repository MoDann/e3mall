package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItemParam;
import cn.e3mall.service.ItemParamService;

/*
 * 商品规格参数Controller
 */
@Controller
public class ItemParamController {
	
	@Autowired
	private ItemParamService itemParamService;
	
	@RequestMapping("/item/param/list")
	@ResponseBody
	public EasyUIDataGridResult getItemParam(int page,int rows) {
		
		EasyUIDataGridResult result = itemParamService.getItemParamList(page,rows);
		return result;
	}
	
	@RequestMapping("/item/param/query/itemcatid/{itemCatId}")
	@ResponseBody
	public E3mallResult getItemParamByCid(@PathVariable Long itemCatId) {
		
		E3mallResult result = itemParamService.getItemParamByCid(itemCatId);
		return result;
	}
	
	@RequestMapping("/item/param/save/{cid}")
	@ResponseBody
	public E3mallResult addItemParam(@PathVariable Long cid,String paramData) {
		
		TbItemParam itemParam = new TbItemParam();
		itemParam.setItemCatId(cid);
		itemParam.setParamData(paramData);
		
		E3mallResult result = itemParamService.addItemParam(itemParam);
		return result;
	}
	
	@RequestMapping("/item/param/delete")
	@ResponseBody
	public E3mallResult deleteItemParam(String ids) {
		
		E3mallResult result = itemParamService.deleteParam(ids);
		return result;
	}

}
