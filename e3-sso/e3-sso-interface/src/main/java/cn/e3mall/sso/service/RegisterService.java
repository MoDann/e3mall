package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.pojo.TbUser;

/**
 * 注册功能Service
 * @author ASUS
 *
 */
public interface RegisterService {

	E3mallResult checkData(String param, int type);
	E3mallResult register(TbUser user);
}
