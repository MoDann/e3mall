package cn.e3mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.common.pojo.EasyUIDataGridResult;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbItemCatMapper;
import cn.e3mall.mapper.TbItemParamMapper;
import cn.e3mall.pojo.ItemParamWithName;
import cn.e3mall.pojo.TbItemCat;
import cn.e3mall.pojo.TbItemCatExample;
import cn.e3mall.pojo.TbItemCatExample.Criteria;
import cn.e3mall.pojo.TbItemParam;
import cn.e3mall.pojo.TbItemParamExample;
import cn.e3mall.service.ItemParamService;

/*
 * 商品规格参数Service
 */

@Service
public class ItemParamServiceImpl implements ItemParamService {
	
	@Autowired
	private TbItemParamMapper itemParamMapper;
	
	@Autowired
	private TbItemCatMapper itemCatMapper;

	@Override
	public EasyUIDataGridResult getItemParamList(int page, int rows) {

		//设置分页信息
		PageHelper.startPage(page, rows);
		TbItemParamExample example = new TbItemParamExample();
		//执行查询规格参数列表
		//执行查询,注意使用withBLOBs，否则查询结果不会附带大文本。
		List<TbItemParam> params = itemParamMapper.selectByExampleWithBLOBs(example);
		//查询规格对的类目名称
		List<ItemParamWithName> paramWithNames = new ArrayList<ItemParamWithName>(params.size());
		for (TbItemParam param : params) {
			String catName = getItemCatName(param.getItemCatId());
			ItemParamWithName itemParamWithName = new ItemParamWithName( param,catName);
		//	System.out.println(itemParamWithName);
			paramWithNames.add(itemParamWithName);
		}
		//分页处理
		PageInfo<TbItemParam> pageInfo = new PageInfo<>(params);
		//封装
		EasyUIDataGridResult result = new EasyUIDataGridResult();
		result.setRows(paramWithNames);
		result.setTotal(pageInfo.getTotal());
		
		return result;
	}

	/*
	 * 根据itemCatId查询ItemCatName
	 */
	private String getItemCatName(Long itemCatId) {

		//设置查询条件
		/*TbItemCatExample example = new TbItemCatExample();
		Criteria criteria = example.createCriteria();
		criteria.andIdEqualTo(itemCatId);
		//执行查询
		List<TbItemCat> list = itemCatMapper.selectByExample(example);
		//获取itemCatName
		if (list!=null && !list.isEmpty()) {
			String name = list.get(0).getName();
			return name;
		}*/
		//执行查询
		TbItemCat itemCat = itemCatMapper.selectByPrimaryKey(itemCatId);
		if (itemCat!=null) {
			String name = itemCat.getName();
			return name;
		}
		return null;
		
	}

	/*
	 * 根据itemCatId查询商品规格参数信息
	 */
	@Override
	public E3mallResult getItemParamByCid(long itemCatId) {

		//设置条件
		TbItemParamExample example = new TbItemParamExample();
		cn.e3mall.pojo.TbItemParamExample.Criteria criteria = example.createCriteria();
		criteria.andItemCatIdEqualTo(itemCatId);
		////执行查询,注意使用withBLOBs，否则查询结果不会附带大文本
		List<TbItemParam> list = itemParamMapper.selectByExampleWithBLOBs(example);
		if (list!=null && !list.isEmpty()) {
			return E3mallResult.ok(list.get(0));
		}
		//TbItemParam itemParam = itemParamMapper.selectByPrimaryKey(itemCatId);
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult addItemParam(TbItemParam itemParam) {

		//补全属性
		itemParam.setCreated(new Date());
		itemParam.setUpdated(new Date());
		//保存
		itemParamMapper.insert(itemParam);
		return E3mallResult.ok();
	}

	@Override
	public E3mallResult deleteParam(String ids) {

		if (StringUtils.isNotEmpty(ids)) {
			String[] split = ids.split(",");
			for (String id : split) {
				itemParamMapper.deleteByPrimaryKey(Long.valueOf(id));
			}
			return E3mallResult.ok();
		}
		return null;
	}
	
	

}
