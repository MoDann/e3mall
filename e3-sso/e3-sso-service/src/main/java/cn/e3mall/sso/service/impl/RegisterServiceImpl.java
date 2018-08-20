package cn.e3mall.sso.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import cn.e3mall.common.util.E3mallResult;
import cn.e3mall.mapper.TbUserMapper;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.pojo.TbUserExample;
import cn.e3mall.pojo.TbUserExample.Criteria;
import cn.e3mall.sso.service.RegisterService;

/**
 * 用户注册Service
 * @author ASUS
 *
 */
@Service
public class RegisterServiceImpl implements RegisterService {

	@Autowired
	private TbUserMapper userMapper;
	
	@Override
	public E3mallResult checkData(String param, int type) {
		//根据不同的type生成不同的查询条件
		TbUserExample example = new TbUserExample();
		Criteria criteria = example.createCriteria();
		//1、用户名   2、手机号   3、邮箱
		if (type == 1) {
			criteria.andUsernameEqualTo(param);
		} else if (type == 2) {
			criteria.andPhoneEqualTo(param);
//		}else if (type == 3) {
//			criteria.andEmailEqualTo(param);
		}else {
			return E3mallResult.build(400, "参数类型错误");
		}
		//执行查询
		List<TbUser> list = userMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return E3mallResult.ok(false);
		}
		return E3mallResult.ok(true);
	}

	@Override
	public E3mallResult register(TbUser user) {
		//对数据进行校验
		if (StringUtils.isBlank(user.getUsername()) || StringUtils.isBlank(user.getPassword())
				|| StringUtils.isBlank(user.getPhone())) {
			return E3mallResult.build(400, "用户数据不完整，注册失败！");
		}
		//1:用户名 2：手机 3:邮箱
		E3mallResult result = checkData(user.getUsername(), 1);
		if (!(boolean)result.getData()) {
			return E3mallResult.build(400, "用户名已存在");
		}
		result = checkData(user.getPhone(), 2);
		if (!(boolean)result.getData()) {
			return E3mallResult.build(400, "手机号已被占用");
		}
		/*result = checkData(user.getEmail(), 3);
		if (!(boolean)result.getData()) {
			return E3mallResult.build(400, "邮箱已被占用");
		}*/
		//补全属性
		user.setCreated(new Date());
		user.setUpdated(new Date());
		//对密码进行加密
		String m5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(m5Pass);
		//插入数据库中
		userMapper.insert(user);
		//返回结果
		return E3mallResult.ok();
	}

}
