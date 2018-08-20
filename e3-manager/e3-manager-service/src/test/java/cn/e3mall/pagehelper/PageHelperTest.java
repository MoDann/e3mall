package cn.e3mall.pagehelper;

import java.util.List;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import cn.e3mall.mapper.TbItemMapper;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbItemExample;

public class PageHelperTest {
	
	@SuppressWarnings("resource")
	@Test
	public void pageHelperTest()throws Exception {
		//初始化Spring容器
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-dao.xml");
		//从容器中获得Mapper代理对象
		TbItemMapper tbItemMapper = applicationContext.getBean(TbItemMapper.class);
		//执行sql语句之前设置分页信息PageHelper的startPage信息
		PageHelper.startPage(1, 10);
		//执行sql语句
		TbItemExample example = new TbItemExample();
		List<TbItem> list = tbItemMapper.selectByExample(example);
		//获取分页信息，总条数、总页数、当前条数、上一页、下一页、首页...
		PageInfo<TbItem> pageInfo = new PageInfo<>(list);
		System.out.println(pageInfo.getTotal());
		System.out.println(pageInfo.getPages());
		System.out.println(pageInfo.getPageNum());
		System.out.println(pageInfo.getPageSize());
		System.out.println(list.size());
		
		
	}

}
