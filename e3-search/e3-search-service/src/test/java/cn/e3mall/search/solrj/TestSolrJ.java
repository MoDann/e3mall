package cn.e3mall.search.solrj;

import java.util.List;
import java.util.Map;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServer;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

public class TestSolrJ {
	
	@Test
	public void addDocument()throws Exception {
		
		//创建一个SolrServer对象，创建一个连接，参数是solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建一个文档对象SolrInputDocument
		SolrInputDocument document = new SolrInputDocument();
		//向文档对象中添加域，必须包含一个id域，且为String类型，其他域的名称必须要在scheme.xml中定义
		document.addField("id", "dom01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", 99999);
		//把文档写入索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void deleteDocument()throws Exception {
		
		//创建一个SolrServer对象，创建一个连接，参数是solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//删除文档
	//	solrServer.deleteById("dom01");
		//删除所有
		solrServer.deleteByQuery("*:*");
		//solrServer.deleteByQuery("id:dom01");
		//提交
		solrServer.commit();
	}
	
	@Test
	public void queryDocument() throws Exception {
		
		//创建一个SolrServer对象，创建一个连接，参数是solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		//query.setQuery("*:*");
		query.set("q", "*:*");
		//执行查询，QueryResponse对象
		QueryResponse queryResponse = solrServer.query(query);
		//获取文档列表，获取总的结果条数
		SolrDocumentList results = queryResponse.getResults();
		System.out.println("总条数："+results.getNumFound());
		//遍历文档列表
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}

	@Test
	public void queryIndexFuza()throws Exception {
		
		//创建一个SolrServer对象，创建一个连接，参数是solr服务的url
		SolrServer solrServer = new HttpSolrServer("http://192.168.25.129:8080/solr/collection1");
		//创建一个SolrQuery对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.set("q", "手机");
		query.set("df", "item_title");
		query.setHighlight(true);//开启高亮
		query.addHighlightField("item_title");
		query.setHighlightSimplePre("<em>");
		query.setHighlightSimplePost("</em>");
		//执行查询，QueryResponse对象
		QueryResponse queryResponse = solrServer.query(query);
		//获取文档列表，获取总的结果条数
		Map<String, Map<String, List<String>>> highlighting = queryResponse.getHighlighting();
		SolrDocumentList results = queryResponse.getResults();
		System.out.println("总条数："+results.getNumFound());
		//遍历文档列表
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			//获取高亮显示
			List<String> list = highlighting.get(solrDocument.get("id")).get("item_title");
			String title = "";
			if (list!=null && list.size()>0) {
				title = list.get(0);
			}else {
				title = (String) solrDocument.get("item_title");
			}
			System.out.println(title);
			System.out.println(solrDocument.get("item_sell_point"));
			System.out.println(solrDocument.get("item_price"));
			System.out.println(solrDocument.get("item_image"));
			System.out.println(solrDocument.get("item_category_name"));
		}
	}
}
