package com.chdp.chdpweb.auth;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.bean.UserAuthority;
import com.chdp.chdpweb.service.UserService;

public class ShiroDbRealm extends AuthorizingRealm {
	@Autowired
	private UserService userService;

	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String currentUsercode = (String) super.getAvailablePrincipal(principals);
		User user = userService.getUser(currentUsercode);
		if (user != null) {
			SimpleAuthorizationInfo simpleAuthorInfo = new SimpleAuthorizationInfo();
			for (UserAuthority auth : userService.getUserAuthority(user)) {
				simpleAuthorInfo.addRole(auth.name());
				return simpleAuthorInfo;
			}
		}

		return null;
	}

	@Override
	protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken)
			throws AuthenticationException {
		UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
		User user = userService.getUser(token.getUsername());
		if (user != null) {
			AuthenticationInfo authcInfo = new SimpleAuthenticationInfo(user.getUsercode(), user.getPassword(),
					this.getName());
			SecurityUtils.getSubject().getSession().setAttribute("user", user);
			return authcInfo;
		}
		return null;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

}
