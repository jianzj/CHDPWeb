package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.bean.UserAuthority;
import com.chdp.chdpweb.dao.UserDao;

@Repository
public class UserService {
	@Autowired
	private UserDao userDao;

	public User getUser(String usercode) {
		return userDao.getUser(usercode);
	}

	public List<UserAuthority> getUserAuthority(User user) {
		List<UserAuthority> auths = new ArrayList<UserAuthority>(11);
		if (user == null)
			return auths;
		if ((user.getAuthority() & 1024) == 1) {
			auths.add(UserAuthority.ADMIN);
		}
		if ((user.getAuthority() & 512) == 1) {
			auths.add(UserAuthority.RECEIVE);
		}
		if ((user.getAuthority() & 256) == 1) {
			auths.add(UserAuthority.CHECK);
		}
		if ((user.getAuthority() & 128) == 1) {
			auths.add(UserAuthority.MIX);
		}
		if ((user.getAuthority() & 64) == 1) {
			auths.add(UserAuthority.MIXCHECK);
		}
		if ((user.getAuthority() & 32) == 1) {
			auths.add(UserAuthority.SOAK);
		}
		if ((user.getAuthority() & 16) == 1) {
			auths.add(UserAuthority.DECOCT);
		}
		if ((user.getAuthority() & 8) == 1) {
			auths.add(UserAuthority.POUR);
		}
		if ((user.getAuthority() & 4) == 1) {
			auths.add(UserAuthority.CLEAN);
		}
		if ((user.getAuthority() & 2) == 1) {
			auths.add(UserAuthority.PACKAGE);
		}
		if ((user.getAuthority() & 1) == 1) {
			auths.add(UserAuthority.SHIP);
		}
		return auths;
	}

	public String encodePassword(String usercode, String password) {
		return DigestUtils.sha256Hex(usercode + password);
	}
}
