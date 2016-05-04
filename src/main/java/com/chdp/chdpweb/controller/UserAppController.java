package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.service.UserService;

@Controller
@RequestMapping("/app/user")
public class UserAppController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	@ResponseBody
	public AppResult login(HttpServletRequest request) {
		String usercode = request.getParameter("usercode");
		String password = request.getParameter("password");

		UsernamePasswordToken token = new UsernamePasswordToken(usercode,
				userService.encodePassword(usercode, password));
		Subject currentUser = SecurityUtils.getSubject();

		AppResult result = new AppResult();
		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			result.setErrorMsg("用户不存在");
		} catch (IncorrectCredentialsException e) {
			result.setErrorMsg("密码不正确");
		} catch (AuthenticationException e) {
			result.setErrorMsg("用户名或密码不正确");
		}

		if (currentUser.isAuthenticated()) {
			result.setSuccess(true);
			result.setSessionId(SecurityUtils.getSubject().getSession().getId().toString());
			return result;
		} else {
			token.clear();
			result.setSuccess(false);
			return result;
		}
	}

	@RequestMapping("/logout")
	@ResponseBody
	public AppResult logout() {
		SecurityUtils.getSubject().logout();
		AppResult result = new AppResult();
		result.setSuccess(true);
		return result;
	}

	@RequestMapping(value = "/getUser")
	@ResponseBody
	public User getUser(HttpServletRequest request) {
		User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
		return user;
	}
}
