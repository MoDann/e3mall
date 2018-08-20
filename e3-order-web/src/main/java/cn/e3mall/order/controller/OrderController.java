package cn.e3mall.order.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import org.json.JSONObject;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;

import cn.e3mall.cart.service.CartService;
import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.order.pojo.OrderInfo;
import cn.e3mall.order.service.OrderService;
import cn.e3mall.order.util.AlipayClientFactory;
import cn.e3mall.pojo.TbItem;
import cn.e3mall.pojo.TbOrder;
import cn.e3mall.pojo.TbUser;

/**
 * 订单管理 Controller
 * @author ASUS
 *
 */
@Controller
public class OrderController {

	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private OrderService orderService;
	
	@Value("${PAY_SUCCESS_URL}")
	private String PAY_SUCCESS_URL;
	
	@RequestMapping("/order/order-cart")
	public String showOrder(HttpServletRequest request) {
		//取用户登录信息
		TbUser user = (TbUser) request.getAttribute("user");
		//根据用户id取收货地址列表
		//使用静态数据。。。
		//取支付方式列表
		//静态数据
		//根据用户id取购物车列表
		List<TbItem> cartList = cartService.getCartList(user.getId());
		if (cartList != null && cartList.size()>0) {
			//返回结果
			request.setAttribute("cartList", cartList);
			return "order-cart";
		}
		return "redirect:http://localhost:8092/cart/cart.html";
	}
	
	@RequestMapping(value="/order/create"/*,method=RequestMethod.POST*/)
	public String createOrder(OrderInfo orderInfo,HttpServletRequest request) {
		//获取用户信息
		TbUser user = (TbUser) request.getAttribute("user");
		//将用户信息插入订单表
		orderInfo.setUserId(user.getId());
		orderInfo.setBuyerNick(user.getUsername());
		//调用服务生成订单
		E3mallResult createOrder = orderService.createOrder(orderInfo);
		//如果订单生成成功
		if (createOrder.getStatus() == 200) {
			//清空购物车
			cartService.clearCart(user.getId());
		}
		//把订单号传递给页面
		request.setAttribute("orderId", createOrder.getData());
		request.setAttribute("payment", orderInfo.getPayment());
		return "success";
	}
	
	@RequestMapping("/success")
	public String showPay() {
		return "success";
	}
	
	@RequestMapping(value="/order/pay",method=RequestMethod.POST)
	public void zhifubao(HttpServletRequest request, HttpServletResponse response/*,String orderId*/) {
		//获取前台订单数据
		String orderId = request.getParameter("orderId");
		//String path = "";
		System.out.println("orderId:"+orderId);
		
		/*if (StringUtils.isBlank(orderId)) {
			path = "/success.action";
		}*/
		
		try {
			TbOrder order = orderService.findOrderById(orderId);
			AlipayClient alipayClient = AlipayClientFactory.getAlipayClient(); // 获得初始化的AlipayClient
			AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();// 创建API对应的request
			//同步
			alipayRequest.setReturnUrl(PAY_SUCCESS_URL);
			//异步
			// alipayRequest.setNotifyUrl(PAY_SUCCESS_URL);//在公共参数中设置回跳和通知地址
			JSONObject json = new JSONObject();
			// 业务参数填充
			/* (必选) 商户订单号,64个字符以内、可包含字母、数字、下划线；需保证在商户端不重复 */
			json.put("out_trade_no", order.getOrderId()); // 订单编号
			/* (必选) 订单标题 */
			json.put("subject", "订单："+order.getOrderId()); // 订单名字
			/* (必选) 订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000] */
			System.out.println(order.getPayment());
			json.put("total_amount", new Double(order.getPayment())+10); // 订单总价钱
			/* (可选) 销售产品码 */
			json.put("product_code", "FAST_INSTANT_TRADE_PAY"); // 产品代码
			/* (可选) 订单描述 */
			json.put("body", "订单付账");
			alipayRequest.setBizContent(json.toString());
			String form = alipayClient.pageExecute(alipayRequest).getBody(); // 调用SDK生成表单
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().write(form);// 直接将完整的表单html输出到页面
			response.getWriter().flush();
			response.getWriter().close();
			//修改状态,1.未付款 2.已付款 3.未发货 4.已发货 5.交易成功 6.交易关闭
			order.setStatus(2);
			orderService.updateOrderStatus(order);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	//	return path;
	}
}
