package cn.e3mall.freemarker;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerTest {
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Test
	public void testFreemarker() throws Exception {
		//1、创建一个模板文件（在WEB-INF下的ftl下的hello.ftl）
		//2、创建一个configuration对象
		Configuration configuration = new Configuration(Configuration.getVersion());
		//3、设置模板文件保存目录
		configuration.setDirectoryForTemplateLoading(new File
				("D:\\Program Files\\eclipse-workspace\\e3-item-web\\src\\main\\webapp\\WEB-INF\\ftl"));
		//4、设置模板文件的字符集，一般为utf-8
		configuration.setDefaultEncoding("utf-8");	
		//5、加载模板文件，创建一个模板对象
		Template template = configuration.getTemplate("hello.ftl");
		//6、创建一个数据集，可以是pojo、map，推荐使用map
		Map map = new HashMap<>();
		//将模板中的字符串替换,以hello为key
		map.put("hello", "hello freemarker!");
		//7、创建一个writer对象，指定输出文件的路径与文件名
		Writer writer = new FileWriter(new File("E:\\项目二：宜立方商城(80-93天）\\freemarker\\hello.txt"));
		//8、生成静态页面
		template.process(map, writer);
		//9、关闭流
		writer.close();
	}

}
