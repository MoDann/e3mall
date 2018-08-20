package cn.e3mall.search.activemq;

import java.io.IOException;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 接收消息测试
 * @author ASUS
 *
 */
public class MessageCustomer {

	@SuppressWarnings("resource")
	@Test
	public void msgCustomer() throws IOException {
		
		//初始化一个Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
		//等待接收消息
		System.in.read();
	}
}
