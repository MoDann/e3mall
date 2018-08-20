package cn.e3mall.sso.service;

import cn.e3mall.common.util.E3mallResult;

/**
 * 取tokenService
 * @author ASUS
 *
 */
public interface TokenService {

	E3mallResult getUserByToken(String token);
}
