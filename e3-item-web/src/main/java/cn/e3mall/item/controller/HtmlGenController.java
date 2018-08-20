package cn.e3mall.item.controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;


/**
 *  获取静态页面Con投入哦哦
 * @author ASUS
 *
 */
@Controller
public class HtmlGenController {

	@Autowired
	private FreeMarkerConfigurer freeMarkerConfigurer;
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping("/gethtml")
	@ResponseBody
	public String getHtml() throws Exception {
		Configuration configuration = freeMarkerConfigurer.getConfiguration();
		//加载模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//创建一个数据集
		Map map = new HashMap<>();
		map.put("hello", "hello freeMarker!!!");
		//指定文件名与输出路径
		Writer writer = new FileWriter(new File("E:\\项目二：宜立方商城(80-93天）\\freemarker\\hello.html"));
		//输出文件
		template.process(map, writer);
		//关闭流
		writer.close();
		return "Ok";
		
	}
}
