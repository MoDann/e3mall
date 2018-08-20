package cn.e3mall.content.service;

import java.util.List;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;

/*
 * 商品分类管理接口
 */
public interface ContentCategoryService {

	//获取商品分类管理节点
	List<EasyUITreeNode> getContentCategoryList(long parentId);
	//添加商品分类管理节点
	E3mallResult addContentCategory(long parentId,String name);
	//重命名商品分类管理节点
	E3mallResult updateContentCategory(long id,String name);
	//删除
	E3mallResult deleteContentCategory(long id);
}
