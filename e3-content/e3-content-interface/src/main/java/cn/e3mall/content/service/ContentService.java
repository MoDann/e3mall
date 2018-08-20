package cn.e3mall.content.service;


import java.util.List;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbContent;

public interface ContentService {

	//根据分类叶子节点id获取内容
	EasyUIDataGridResult getContent(long categoryId,int page,int rows);
	
	//添加商品内容
	E3mallResult addContent(TbContent content);
	
	//更新商品内容
	E3mallResult updateConten(TbContent content);
	
	//删除
	E3mallResult deleteContent(String ids);
	
	//根据CategoryId查询商品内容
	List<TbContent> getContentByCid(long cid);
}
