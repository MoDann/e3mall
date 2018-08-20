package cn.e3mall.search.service.impl;

import java.util.List;

import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.common.SolrInputDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.search.mapper.ItemMapper;
import cn.e3mall.search.service.SearchItemService;


/*
 *  索引库维护service
 */
@Service
public class SearchItemServiceImpl implements SearchItemService {

	@Autowired
	private ItemMapper itemMapper;
	
	@Autowired
	private SolrServer solrServer;
	
	@Override
	public E3mallResult importAllItems() {

		try {
			//获取商品列表
			List<SearchItem> itemList = itemMapper.getItemList();
			//遍历商品列表
			for (SearchItem searchItem : itemList) {
				//创建一个文档对象
				SolrInputDocument document = new SolrInputDocument();
				//向文档对象中添加域，必须要有一个id域
				document.addField("id", searchItem.getId());
				document.addField("item_title", searchItem.getTitle());
				document.addField("item_sell_point", searchItem.getSell_point());
				document.addField("item_price", searchItem.getPrice());
				document.addField("item_image", searchItem.getImage());
				document.addField("item_category_name", searchItem.getCategory_name());
				//把文档对象导入索引库
				solrServer.add(document);
			}
			//提交
			solrServer.commit();
			//返回结果
			return E3mallResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return E3mallResult.build(500, "导入时发生异常");
		}
		
	}

}
