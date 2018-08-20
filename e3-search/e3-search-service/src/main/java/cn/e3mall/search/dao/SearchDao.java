package cn.e3mall.search.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.alibaba.dubbo.common.utils.StringUtils;

import cn.e3mall.common.pojo.SearchItem;
import cn.e3mall.common.pojo.SearchResult;

/**
 * 商品搜索Dao
 *
 */
@Repository
public class SearchDao {
	
	@Autowired
	private SolrServer solrServer;

	/*
	 * 根据条件查询引擎库
	 */
	public SearchResult search(SolrQuery query) throws Exception {
		
		//根据query查询搜索库
		QueryResponse queryResponse = solrServer.query(query);
		//取查询结果
		SolrDocumentList documentList = queryResponse.getResults();
		//取查询总记录
		long numFound = documentList.getNumFound();
		SearchResult result = new SearchResult();
		result.setRecordCount(numFound);
		//取商品列表，取高亮显示
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		List<SearchItem> itemList = new ArrayList<>();
		for (SolrDocument solrDocument : documentList) {
			SearchItem searchItem = new SearchItem();
			Object id = solrDocument.get("id");
			searchItem.setId((String)id);
			String title = "";
			List<String> list = highlighting.get(id).get("item_title");
			if (list != null && list.size()>0) {
				title = list.get(0);
			} else {
				title = (String) solrDocument.get("item_title");
			}
			searchItem.setTitle(title);
			searchItem.setCategory_name((String) solrDocument.get("item_category_name"));
			searchItem.setImage((String)solrDocument.get("item_image"));
			searchItem.setPrice((long) solrDocument.get("item_price"));
			searchItem.setSell_point((String) solrDocument.get("item_sell_point"));
			itemList.add(searchItem);
		}
		result.setItemList(itemList);
		//返回结果列表
		return result;
	}
}
