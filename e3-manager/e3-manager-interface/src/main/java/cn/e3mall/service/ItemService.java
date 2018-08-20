package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.pojo.TbItemParamItem;

public interface ItemService {
	
	//根据id获取商品信息
	TbItem getItemById(long itemId);
	
	//获取商品描述信息
	E3mallResult getItemDesc(long itemId);
	
	//获取商品规格信息
	E3mallResult getItemParamItem(long itemId);
	
	//获取商品分页查询信息
	EasyUIDataGridResult getItemList(int page,int rows);
	
	//保存商品信息,分别插入到商品表，描述表，商品规格表
	E3mallResult addItem(TbItem item,String desc,TbItemParamItem itemParamItem);
	
	//修改商品信息
	E3mallResult updateItem(TbItem item,String desc,String paramData);
	
	//删除商品信息
	E3mallResult deleteItem(String ids);
	
	//商品下架
	E3mallResult instockItem(String ids);
	
	//商品上架
	E3mallResult reshelfItem(String ids);

	//根据商品id取商品描述
	TbItemDesc getItemDescById(long itemId);

}
