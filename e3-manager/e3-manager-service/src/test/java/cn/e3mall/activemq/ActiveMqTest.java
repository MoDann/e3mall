package cn.e3mall.activemq;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

public class ActiveMqTest {

	/**
	 * 使用Queue发送消息，点对点
	 */
	@Test
	public void testQueueProducer() throws Exception {
		
		//1、创建一个连接工厂ConectionFactory对象，包含服务Ip与端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2、使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection的start方法
		connection.start();
		//4、使用连接对象创建一个Session会话对象
		//第一个参数：是否开启事务，如果开启事务，第二个参数无意义，一般不开启事务
		//第二个参数：应答模式，自动应答与手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session创建一个Destination（目的地）对象，两种形式：topic与queue
		Queue queue = session.createQueue("text-queue");
		//6、使用Session创建一个Message对象，可以使用TextMessage
			//法1
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq");*/
			//法2
		TextMessage textMessage = session.createTextMessage("hello activemq");
		//7、创建一个producer（生产者）对象
		MessageProducer producer = session.createProducer(queue);
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	/**
	 * 接收消息
	 */
	@Test
	public void testQueueConsumer()throws Exception {
		
		//创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//使用连接工厂创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session创建一个destination（目的地）对象，queue对象
		Queue queue = session.createQueue("text-queue");
		//使用Session创建一个Consumer（消费者）对象
		MessageConsumer consumer = session.createConsumer(queue);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		//等待
		System.in.read();
		//关闭连接
		consumer.close();
		session.close();
		connection.close();
	}
	
	/**
	 * 使用Topic发送消息,广播形式
	 * 没人接收就会消失（即没有消费者在启动状态）
	 */
	@Test
	public void testTopicProducer() throws Exception {
		
		//1、创建一个连接工厂ConectionFactory对象，包含服务Ip与端口
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//2、使用工厂对象创建一个Connection对象
		Connection connection = connectionFactory.createConnection();
		//3、开启连接，调用Connection的start方法
		connection.start();
		//4、使用连接对象创建一个Session会话对象
		//第一个参数：是否开启事务，如果开启事务，第二个参数无意义，一般不开启事务
		//第二个参数：应答模式，自动应答与手动应答，一般自动应答
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//5、使用Session创建一个Destination（目的地）对象，两种形式：topic与queue
		Topic topic = session.createTopic("text-topic");
		//6、使用Session创建一个Message对象，可以使用TextMessage
			//法1
		/*TextMessage textMessage = new ActiveMQTextMessage();
		textMessage.setText("hello activemq");*/
			//法2
		TextMessage textMessage = session.createTextMessage("topic massage");
		//7、创建一个producer（生产者）对象
		MessageProducer producer = session.createProducer(topic);
		//8、发送消息
		producer.send(textMessage);
		//9、关闭资源
		producer.close();
		session.close();
		connection.close();
	}
	
	/**
	 * 接收消息
	 */
	@Test
	public void testTopicConsumer()throws Exception {
		
		//创建一个ConnectionFactory对象
		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://192.168.25.129:61616");
		//使用连接工厂创建一个connection对象
		Connection connection = connectionFactory.createConnection();
		//开启连接
		connection.start();
		//使用连接对象创建一个Session对象
		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
		//使用Session创建一个destination（目的地）对象，queue对象
		Topic topic = session.createTopic("text-topic");
		//使用Session创建一个Consumer（消费者）对象
		MessageConsumer consumer = session.createConsumer(topic);
		//接收消息
		consumer.setMessageListener(new MessageListener() {
			
			@Override
			public void onMessage(Message message) {
				//打印结果
				TextMessage textMessage = (TextMessage) message;
				try {
					String text = textMessage.getText();
					System.out.println(text);
				} catch (JMSException e) {
					e.printStackTrace();
				}
			}
		});
		System.out.println("消费者2已经启动。。。");
		//等待接收消息
		System.in.read();
		//关闭连接
		consumer.close();
		session.close();
		connection.close();
	}
	
}
