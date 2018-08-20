package cn.e3mall.order.util;

public class AlibabaMainConfig {

	/**
	 * 支付宝网关 (固定)
	 */
	public static final String serverUrl = "https://openapi.alipaydev.com/gateway.do"; 
	/**
	 * APPID 即创建应用后生成
	 */
	public static final String appId = "2016091800540776"; 
	/**
	 * 开发者私钥，由开发者自己生成
	 */
	public static final String privateKey = 
			"MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCfrEjTygnYXNp8zgErpfdgqA/o3UG5R8Hyhb4poRsqHrjZTcX6+ITXrRXRDCq1ip+KMPRiolwPlvqBSrdXWe1Rd3r0fjiPTxbEj/rl3Or6xaurwog/sAAng7OIzFEpbtTiKJG9FboSuh97AFg3//qguuABX23PBRnSLj2YLoN3kmIY9O/8wnyUaFGa4mMPk8lOeY/JwdGc7c2jclsWH/gKAOAfig+TsTDmpKe2+bUFyq7/g7r7n0w3jXz3k3wsmCCZI+oOnrWGCx/QBETQYt3dH3lMLwoD10B9W4CbXnHm0ULn85v44kXu/VuoRI6/RgZWNg5H+p672X6hHIiYj2c9AgMBAAECggEAKlMkyt8XVP7n+4Uv0j4gIMLr1LIxpcdbpcnF6osGBFg4b4GqJSiTLgh3a/5po82nFJ59aVfYR1mh9Xu0tbbkrV+afAypOXOheWEhBKVWtDqJkvHx48/nepHymSRvryftzjUqzJBhzmz/wz8II2/7I3flwPdmyCV1/ry7HwfVWXBMAgJdmYI8OiJ1Tcc9JH/sB3HFPVEaB6r8/ctrJpQ+gkIaIyLxU1kk8wiSmxDIPh+5PF5V5UXMCbY+scAQqPdjWPma7qbpm4865ljX+6uJIXZOYxt6R+EkfjHyGAnX56NgXMxYYCiMYHgg1vIGeHFLYhS35bLp/oNq9xRsT8MmgQKBgQDlIGGZt59b7Qot0M1gMH32I5tmOaUJrmI6Jis2ZaexynfdS2GRKuv8a4UGTuE0Sqe9Hx3xAvcDu1dvGi1gmMrH8Cwfzcz7BZOZOWo0LJWB41ynUD4iNRM3kPtiobxhLQuGcTpMoEHnWMuaJeUFutLF/BK4R8s7wKAfbqJL8GTHBQKBgQCyZohg//8lL+eb25Sz8oWEx4SsUT36FUHidvb2rrXbObDLG2hEFBaasJCLuFRXuJozbCnd9afZ6SroURFtM5o8a3lLZhrCEcFKBzD9aoOqatOfNhe65paVGvchiqPGjOzvVMLIaaXnB8ZWiXae97ixEKGvj+ldLwQuaZm5B24k2QKBgHTxPv8+ueYoKH7TyE6k0VVUPRdCqpozV80H+BwubBWRGfZnZ+e4uXM+yUk77zNKOyyKejStZ7eO4YRCHxDjSvQa/9pHpqwj335f0HXKn5AIpBx9FwlMf39nzvY7SDNs0yoOFkxPqvNR3bDSUH7JKJSGIGPPuMnJebAxPkJ18mmlAoGABKY/b6e8Uo/JDXrPiVEMwcUhZAxJ/TaInQpUrWEoRCpgvSpdsUiEvktTuRB4wkCBZE2xWaOJWFiHUb6rIZlyr5htdNWYZO5phzBgynn4LHzUPxX90FUqH4CwaCNg2U/a7PHckbSPKlI9jQvQF5yFJ7gzPowVnOQYy8zcwMG80SECgYADGeF6DO/jWAYJNulDT6ULkskkQ37U1nmMz0Wt35t0AZvxn/lzfJx7AU2KoreRdUW2WqRCbWt0szqEIM9ebj1QcCUpix28ISe6qSahRDun/A0eXKb/9jL4g3LQqxV8T9xvM57MDy3ZHRA18BzqiEB9e+k1KLZLzNNkGx5pA8A+sA==";
	/**
	 * 	参数返回格式，只支持json json（固定）
	 */
	public static final String format = "json";
	/**
	 * 编码集，支持GBK/UTF-8
	 */
	public static final String charset = "utf-8";
	/**
	 * 支付宝公钥，由支付宝生成
	 */
	public static final String alipayPulicKey = 
			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAvSrxDhOu3UVPYP7GYPHKsV4UlBWN7C1KI0Xf5UUQrSgiFHf8a5kN5rnENhwxVXxRmYnZPRItdcSiKEsZoSRugFUwu7aawyiwzB4N71W+QxSyQCtQgQE2CZgzd3LDWWxX9M5Y55lUnLOF2W4h+KLpjqxF0cuIvuQ9MVSKfEkTiErJOTYjpVC0I0GKDVT2tHE+PzoGgo0Qs+2GVg3mr8n2GvaakZnBM7WcnbEMRuTa9lFyvantzpPfdqIj8e1qtSO4pmy5fEGaOMNfsqIW3fO/cH5kgOAWfld4Ol5l5pRHiIoTqnNjW2YOLBoM5L6/s9V0kS7JwJBcFW9G1GkZ/85AQQIDAQAB";
	/**
	 * 生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	 */
	public static final String signType = "RSA2";
	
	/**
	 * 日志记录目录
	 */
	public static String log_path = "/log";
}
