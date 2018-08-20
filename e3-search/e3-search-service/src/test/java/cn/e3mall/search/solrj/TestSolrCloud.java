package cn.e3mall.search.solrj;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.CloudSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.junit.Test;

/*
 * Solr集群测试
 */
public class TestSolrCloud {

	@Test
	public void addSolrCloud()throws Exception {
		
		//创建一个集群连接SolrServer对象,zHost:zookeeper的地址与端口号
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.129:2182,192.168.25.129:2183,192.168.25.129:2184");
		//设置一个defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//创建一个文档对象
		SolrInputDocument document = new SolrInputDocument();
		//向文档中添加域
		document.addField("id", "solrcloud01");
		document.addField("item_title", "测试商品01");
		document.addField("item_price", "1234");
		//把文档提交到索引库
		solrServer.add(document);
		//提交
		solrServer.commit();
	}
	
	@Test
	public void querySolrDocument() throws Exception {
		
		//创建一个集群连接SolrServer对象,zHost:zookeeper的地址与端口号
		CloudSolrServer solrServer = new CloudSolrServer("192.168.25.129:2182,192.168.25.129:2183,192.168.25.129:2184");
		//设置一个defaultCollection属性
		solrServer.setDefaultCollection("collection2");
		//创建一个查询对象
		SolrQuery query = new SolrQuery();
		//设置查询条件
		query.setQuery("*:*");
		//执行查询
		QueryResponse queryResponse = solrServer.query(query);
		//获取结果
		SolrDocumentList results = queryResponse.getResults();
		System.out.println("总记录数："+results.getNumFound());
		for (SolrDocument solrDocument : results) {
			System.out.println(solrDocument.get("id"));
			System.out.println(solrDocument.get("item_title"));
			System.out.println(solrDocument.get("item_price"));
		}
	}
}
