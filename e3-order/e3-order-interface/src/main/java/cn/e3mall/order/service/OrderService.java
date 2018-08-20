package cn.e3mall.order.service;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbOrderItem;

/**
 * 订单管理Service
 * @author ASUS
 *
 */
public interface OrderService {

	E3mallResult createOrder(OrderInfo orderInfo);
	
	TbOrder findOrderById(String orderId);
	
	TbOrderItem findOrderItemByOrderId(String orderId);
	
	void updateOrderStatus(TbOrder order);
}
