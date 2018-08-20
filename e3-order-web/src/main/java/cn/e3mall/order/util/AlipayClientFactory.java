package cn.e3mall.order.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeCancelRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.response.AlipayTradeCancelResponse;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;

public class AlipayClientFactory {

	
	/**
	 * alipayClient只需要初始化一次，后续调用不同的API都可以使用同一个alipayClient对象。 
	 * @return
	 */
	public static AlipayClient getAlipayClient() {
		return AlipatClientSingletonFactory.alipayClient;
	}
	
	private static class AlipatClientSingletonFactory{
		public static final AlipayClient alipayClient = new DefaultAlipayClient(AlibabaMainConfig.serverUrl,
				AlibabaMainConfig. appId, AlibabaMainConfig.privateKey, AlibabaMainConfig.format,
				AlibabaMainConfig.charset, AlibabaMainConfig.alipayPulicKey, AlibabaMainConfig.signType);
	}
	
	/**
	 *  查询订单状态
	 * @param appAuthToken
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradeQueryResponse query(String appAuthToken, String bizContent) throws AlipayApiException {
		AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
		request.putOtherTextParam("app_auth_token", appAuthToken);
		request.setBizContent(bizContent);
		return AlipayClientFactory.getAlipayClient().execute(request);
	}

	/**
	 * 条码支付
	 * @param appAuthToken
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradePayResponse pay(String appAuthToken, String bizContent) throws AlipayApiException {
		AlipayTradePayRequest request = new AlipayTradePayRequest();
		request.putOtherTextParam("app_auth_token", appAuthToken);
		request.setBizContent(bizContent);
		return AlipayClientFactory.getAlipayClient().execute(request);
	}

	/**
	 *  扫码支付
	 * @param appAuthToken
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradePrecreateResponse precreate(String appAuthToken, String bizContent) throws AlipayApiException {
		AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
		request.putOtherTextParam("app_auth_token", appAuthToken);
		request.setBizContent(bizContent);
		return AlipayClientFactory.getAlipayClient().execute(request);
	}

	/**
	 *  订单撤销
	 * @param appAuthToken
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradeCancelResponse cancel(String appAuthToken, String bizContent) throws AlipayApiException {
		AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
		request.putOtherTextParam("app_auth_token", appAuthToken);
		request.setBizContent(bizContent);
		return AlipayClientFactory.getAlipayClient().execute(request);
	}
	 
	/**
	 * 申请退款
	 * 
	 * @param appAuthToken
	 * @param bizContent
	 * @return
	 * @throws AlipayApiException
	 */
	public AlipayTradeRefundResponse refund(String appAuthToken, String bizContent) throws AlipayApiException {
		AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
		request.putOtherTextParam("app_auth_token", appAuthToken);
		request.setBizContent(bizContent);
		return AlipayClientFactory.getAlipayClient().execute(request);
	}


}
