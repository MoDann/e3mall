package cn.e3mall.order.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.OrderUtils;
import org.springframework.stereotype.Service;

import cn.e3mall.common.jedis.JedisClient;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbOrderItemMapper;
import cn.e3mall.mapper.TbOrderMapper;
import cn.e3mall.mapper.TbOrderShippingMapper;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;
import cn.e3mall.pojo.TbOrderShipping;


/**
 *  订单管理Service
 * @author ASUS
 *
 */
@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private TbOrderMapper orderMapper;
	@Autowired
	private TbOrderShippingMapper orderShippingMapper;
	@Autowired
	private TbOrderItemMapper orderItemMapper;
	@Autowired
	private JedisClient jedisClient;
	
	@Value("${ORDER_ID_GEN_KEY}")
	private String ORDER_ID_GEN_KEY;
	
	@Value("${ORDER_ID_START}")
	private String ORDER_ID_START;
	
	@Value("${ORDER_DETAIL_ID_KEY}")
	private String ORDER_DETAIL_ID_KEY;
	
	@Override
	public E3mallResult createOrder(OrderInfo orderInfo) {

		//生成订单号，使用redis的insr（自增序列号）
		if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
			jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
		}
		String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
		//补全OrderInfo属性
		orderInfo.setOrderId(orderId);
		//1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
		orderInfo.setStatus(1);
		orderInfo.setCreateTime(new Date());
		orderInfo.setUpdateTime(new Date());
		//插入订单表
		orderMapper.insert(orderInfo);
		//向订单明细表中 插入 数据
		List<TbOrderItem> orderItems = orderInfo.getOrderItems();
		for (TbOrderItem tbOrderItem : orderItems) {
			//生成订单明细id
			String odId = jedisClient.incr(ORDER_DETAIL_ID_KEY).toString();
			//补全属性
			tbOrderItem.setId(odId);
			tbOrderItem.setOrderId(orderId);
			//插入
			orderItemMapper.insert(tbOrderItem);
		}
		//向订单物流表中插入数据
		TbOrderShipping orderShipping = orderInfo.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(new Date());
		orderShipping.setUpdated(new Date());
		orderShippingMapper.insert(orderShipping);
		//返回，包含订单号
		return E3mallResult.ok(orderId);
	}

	@Override
	public TbOrder findOrderById(String orderId) {
		//根据订单id查询订单信息
		TbOrder order = orderMapper.selectByPrimaryKey(orderId);
		return order;
	}

	@Override
	public TbOrderItem findOrderItemByOrderId(String orderId) {
		TbOrderItem orderItem = orderItemMapper.selectByPrimaryKey(orderId);
		return orderItem;
	}

	@Override
	public void updateOrderStatus(TbOrder order) {
		orderMapper.updateByPrimaryKey(order);
	}

}
