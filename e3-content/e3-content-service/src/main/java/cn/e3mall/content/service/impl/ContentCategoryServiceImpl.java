package cn.e3mall.content.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.EasyUITreeNode;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.content.service.ContentCategoryService;
import cn.e3mall.mapper.TbContentCategoryMapper;
import cn.e3mall.pojo.TbContentCategory;
import cn.e3mall.pojo.TbContentCategoryExample;
import cn.e3mall.pojo.TbContentCategoryExample.Criteria;


/*
 * 商品内容分类管理
 */
@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {

	@Autowired
	private TbContentCategoryMapper contentCategoryMapper;
	
	@Override
	public List<EasyUITreeNode> getContentCategoryList(long parentId) {

		//根据parentId查询子节点
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		//设置查询条件
		criteria.andParentIdEqualTo(parentId);
		//执行查询
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		//转化为EasyUITreeNode列表
		List<EasyUITreeNode> nodeList = new ArrayList<>();
		for (TbContentCategory tbContentCategory : list) {
			EasyUITreeNode node = new EasyUITreeNode();
			node.setId(tbContentCategory.getId());
			node.setText(tbContentCategory.getName());
			node.setState(tbContentCategory.getIsParent()?"closed":"open");
			nodeList.add(node);
		}
		
		return nodeList;
	}

	@Override
	public E3mallResult addContentCategory(long parentId, String name) {

		//创建一个TBContentCategory对象
		TbContentCategory contentCategory = new TbContentCategory();
		//设置pojo的属性（id不需要）
		contentCategory.setCreated(new Date());
		contentCategory.setName(name);
		//默认排序
		contentCategory.setSortOrder(1);
		contentCategory.setUpdated(new Date());
		//新添加的节点一定是叶子节点
		contentCategory.setIsParent(false);
		//1-正常，2-删除
		contentCategory.setStatus(1);
		contentCategory.setParentId(parentId);
		//插入到数据库
		contentCategoryMapper.insert(contentCategory);
		//根据parentId查询父节点
		TbContentCategory category = contentCategoryMapper.selectByPrimaryKey(parentId);
		//判断是否为父节点（isParent），如果不是TRUE，要改为true
		if (!category.getIsParent()) {
			category.setIsParent(true);
			//执行更新
			contentCategoryMapper.updateByPrimaryKeySelective(category);
		}
		//返回E3mallResult，包含pojo
		return E3mallResult.ok(contentCategory);
	}

	//重命名
	@Override
	public E3mallResult updateContentCategory(long id, String name) {

		//根据id获取TbContenCatagory对象
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//判断新的name值与原来的值是否相同，如果相同则不用更新
		if (name!=null&&name.equals(contentCategory.getName())) {
			return E3mallResult.ok();
		}
		//设置更新数据
		contentCategory.setName(name);
		contentCategory.setUpdated(new Date());
		//更新数据库
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		//返回结果
		return E3mallResult.ok();
	}

	
	//通过父节点id来查询所有子节点，这是抽离出来的公共方法
	private List<TbContentCategory> getContentCategoryListByParentId(long parentId){
		
		TbContentCategoryExample example = new TbContentCategoryExample();
		Criteria criteria = example.createCriteria();
		criteria.andParentIdEqualTo(parentId);
		List<TbContentCategory> list = contentCategoryMapper.selectByExample(example);
		return list;
	}	
		
	//递归删除节点
	private void deleteNode(long parentId){
		List<TbContentCategory> list = getContentCategoryListByParentId(parentId);
		for(TbContentCategory contentCategory : list){
			contentCategory.setStatus(2);
			contentCategoryMapper.updateByPrimaryKey(contentCategory);
			if(contentCategory.getIsParent()){
				deleteNode(contentCategory.getId());
			}
		}
	}

	//删除
	@Override
	public E3mallResult deleteContentCategory(long id) {

		//删除分类，就是改节点的状态为2(1-正常，2-删除)
		TbContentCategory contentCategory = contentCategoryMapper.selectByPrimaryKey(id);
		//状态。可选值:1(正常),2(删除)
		contentCategory.setStatus(2);
		contentCategoryMapper.updateByPrimaryKey(contentCategory);
		//我们还需要判断一下要删除的这个节点是否是父节点，如果是父节点，那么就级联
		//删除这个父节点下的所有子节点（采用递归的方式删除）
		if(contentCategory.getIsParent()){
			deleteNode(contentCategory.getId());
		}
		//需要判断父节点当前还有没有子节点，如果有子节点就不用做修改
		//如果父节点没有子节点了，那么要修改父节点的isParent属性为false即变为叶子节点
		TbContentCategory parent = contentCategoryMapper.selectByPrimaryKey(contentCategory.getParentId());
		List<TbContentCategory> list = getContentCategoryListByParentId(parent.getId());
		//判断父节点是否有子节点是判断这个父节点下的所有子节点的状态，如果状态都是2就说明
		//没有子节点了，否则就是有子节点。
		boolean flag = false;
		for(TbContentCategory tbContentCategory : list){
			if(tbContentCategory.getStatus() == 0){
				flag = true;
				break;
			}
		}
		//如果没有子节点了
		if(!flag){
			parent.setIsParent(false);
			contentCategoryMapper.updateByPrimaryKey(parent);
		}
		//返回结果
		return E3mallResult.ok();
	}

}
