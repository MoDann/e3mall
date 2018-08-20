package cn.e3mall.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParamItem;
import cn.e3mall.service.ItemService;

/*
 * 商品管理Controller
 */
@Controller
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	/*
	 * 根据id获取商品信息
	 */
	@RequestMapping("/item/{itemId}")
	//将结果自动转换为json数据
	@ResponseBody
	//不导入Jackson架包，会报406错误
	public TbItem geItemById(@PathVariable Long itemId) {
		TbItem item = itemService.getItemById(itemId);
		return item;
	}
	
	/*
	 * 根据分页条件查询
	 */
	@RequestMapping("/item/list")
	@ResponseBody
	public EasyUIDataGridResult getItemList(Integer page,Integer rows) {
		
		EasyUIDataGridResult result = itemService.getItemList(page, rows);
		return result;
	}
	
	/*
	 * 保存商品信息
	 */
	@RequestMapping(value="/item/save",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult addItem(TbItem item,String desc,TbItemParamItem itemParamItem) {
		
		E3mallResult result = itemService.addItem(item,desc,itemParamItem);
		return result;
	}
	
	/**
	 * 异步重新加载回显描述
	 * @param id
	 * @return
	 */
	@RequestMapping("/rest/item/query/item/desc/{id}")
	@ResponseBody
	public E3mallResult selectTbItemDesc(@PathVariable long id){
		E3mallResult result = itemService.getItemDesc(id);
	    return result;
	}

	/**
	 * 异步重新加载商品信息
	 * @param id
	 * @return
	 */
	@RequestMapping("/rest/item/param/item/query/{id}")
	@ResponseBody
	public E3mallResult queryById(@PathVariable long id){
	    E3mallResult result = itemService.getItemParamItem(id);
	    return result;
	}
	
	/*
	 * 修改商品信息
	 */
	@RequestMapping(value="/item/update",method=RequestMethod.POST)
	@ResponseBody
	public E3mallResult updateItem(TbItem item,String desc,String paramData) {
		
		E3mallResult result = itemService.updateItem(item,desc,paramData);
		return result;
	}
	
	/*
	 * 删除商品信息
	 */
	@RequestMapping("/item/delete")
	@ResponseBody
	public E3mallResult deleteItem(String ids) {
		
		E3mallResult result = itemService.deleteItem(ids);
		return result;
	}
	
	/*
	 * 商品下架
	 */
	@RequestMapping("/item/instock")
	@ResponseBody
	public E3mallResult instockItem(String ids) {
		
		E3mallResult result = itemService.instockItem(ids);
		return result;
	}
	
	/*
	 * 商品上架
	 */
	@RequestMapping("/item/reshelf")
	@ResponseBody
	public E3mallResult reshelf(String ids) {
		
		E3mallResult result = itemService.reshelfItem(ids);
		return result;
	}
	
}
