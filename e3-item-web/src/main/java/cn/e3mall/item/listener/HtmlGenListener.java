package cn.e3mall.item.listener;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import cn.e3mall.item.pojo.Item;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemDesc;
import cn.e3mall.service.ItemService;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 *  商品详情展示静态html
 * @author ASUS
 *
 */
public class HtmlGenListener implements MessageListener {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@Autowired
	private ItemService itemService;
	
	@Value("${HTML_GEN_PATH}")
	private String HTML_GEN_PATH;
	
	@SuppressWarnings({ "rawtypes", "unchecked", "resource" })
	@Override
	public void onMessage(Message message) {
		//创建一个模板，参考jsp
		//从消息中获取商品id
		TextMessage textMessage = (TextMessage) message;
		
		try {
			String text = textMessage.getText();
			Long itemId = new Long(text);
			//等待事务提交
			Thread.sleep(1000);
			//根据商品id查询商品信息
			TbItem tbItem = itemService.getItemById(itemId);
			Item item = new Item(tbItem);
			//获取商品描述信息
			TbItemDesc itemDesc = itemService.getItemDescById(itemId);
			//创建一个数据集，把商品封装
			Map map = new HashMap<>();
			map.put("item", item);
			map.put("itemDesc", itemDesc);
			//加载模板对象
			Configuration configuration = freeMarkerConfigurer.getConfiguration();
			Template template = configuration.getTemplate("item.ftl");
			//创建一个输出流，指定输出的目录及文件
			Writer writer = new FileWriter(new File(HTML_GEN_PATH + itemId + ".html"));
			//生成静态页面
			template.process(map, writer);
			//关闭流
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
