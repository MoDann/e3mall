package cn.e3mall.service;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbItemParam;

/*
 * 商品规格参数Service
 */
public interface ItemParamService {

	//获取商品分页查询信息
	EasyUIDataGridResult getItemParamList(int page,int rows);
	
	//根据itemCatId查询商品规格信息
	E3mallResult getItemParamByCid(long itemCatId);
	//添加商品规格参数
	E3mallResult addItemParam(TbItemParam itemParam);
	
	//删除商品规格信息
	E3mallResult deleteParam(String ids);
}
