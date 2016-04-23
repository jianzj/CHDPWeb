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
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		if (SecurityUtils.getSubject().isAuthenticated())
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
		return "login";
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request) {
		String usercode = request.getParameter("usercode");
		String password = request.getParameter("password");

		UsernamePasswordToken token = new UsernamePasswordToken(usercode,
				userService.encodePassword(usercode, password));
		Subject currentUser = SecurityUtils.getSubject();
		try {
			currentUser.login(token);
		} catch (UnknownAccountException e) {
			e.printStackTrace();
			request.setAttribute("message_login", "用户不存在");
		} catch (IncorrectCredentialsException e) {
			e.printStackTrace();
			request.setAttribute("message_login", "密码不正确");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			request.setAttribute("message_login", "用户名或密码不正确");
		}

		if (currentUser.isAuthenticated()) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
		} else {
			token.clear();
			return "login";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityUtils.getSubject().logout();
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
	}
}
