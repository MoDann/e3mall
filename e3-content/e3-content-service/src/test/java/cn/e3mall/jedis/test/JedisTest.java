package cn.e3mall.jedis.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

public class JedisTest {
	
	@Test
	public void testJedis() throws Exception {
		//创建一个Jedis对象
		Jedis jedis = new Jedis("192.168.25.129",6379);
		//直接使用Jedis操作redis，所有的redis命名对应一个方法
		jedis.set("test123", "my first jedis test");
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭
		jedis.close();
	}

	
	@Test 
	public void testJedisPool() throws Exception {
		
		//创建一个连接池对象
		JedisPool jedisPool = new JedisPool("192.168.25.129",6379);
		//从连接池获取一个对象，就是一个Jedis对象
		Jedis jedis = jedisPool.getResource();
		//使用Jedis对象操作redis
		String string = jedis.get("test123");
		System.out.println(string);
		//关闭Jedis,每次使用完毕后关闭连接，连接池回收
		jedis.close();
		//关闭连接池
		jedisPool.close();
		
	}
	

	@Test
	public void testJedisCluster() throws Exception {
		
		//创建一个JedisCluster对象，参数是一个nodes的set对象。set中是一个HostAndPort的对象
		Set<HostAndPort> nodes = new HashSet<>();
		nodes.add(new HostAndPort("192.168.25.129", 7001));
		nodes.add(new HostAndPort("192.168.25.129", 7002));
		nodes.add(new HostAndPort("192.168.25.129", 7003));
		
		JedisCluster cluster = new JedisCluster(nodes);
		//直接用JedisCluster操作redis对象
		cluster.set("test", "123");
		String string = cluster.get("test");
		System.out.println(string);
		//关闭
		cluster.close();
	}
}
