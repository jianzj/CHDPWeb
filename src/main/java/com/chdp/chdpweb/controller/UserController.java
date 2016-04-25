package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.service.UserService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login() {
		if (SecurityUtils.getSubject().isAuthenticated())
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
		return "user/login";
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
			request.setAttribute("errorMsg", "用户不存在");
		} catch (IncorrectCredentialsException e) {
			request.setAttribute("errorMsg", "密码不正确");
		} catch (AuthenticationException e) {
			request.setAttribute("errorMsg", "用户名或密码不正确");
		}

		if (currentUser.isAuthenticated()) {
			return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
		} else {
			token.clear();
			return "user/login";
		}
	}

	@RequestMapping("/logout")
	public String logout(HttpServletRequest request) {
		SecurityUtils.getSubject().logout();
		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.GET)
	public String changePassword() {
		return "user/changePassword";
	}

	@RequestMapping(value = "/changePassword", method = RequestMethod.POST)
	public String changePassword(HttpServletRequest request) {
		String password = request.getParameter("password");
		String newPassword = request.getParameter("newPassword");
		String renewPassword = request.getParameter("renewPassword");

		if (!newPassword.equals(renewPassword)) {
			request.setAttribute("errorMsg", "两次密码输入不同");
		} else {
			if (!userService.checkPassword(password)) {
				request.setAttribute("errorMsg", "当前密码错误");
			} else {
				if (userService.changePassword(newPassword))
					request.setAttribute("successMsg", "密码修改成功");
				else
					request.setAttribute("errorMsg", "密码修改失败");
			}
		}

		return "user/changePassword";
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "用户管理");
		List<User> userList = userService.getUserList(pageNum);
		request.setAttribute("userList", userList);
		PageInfo<User> page = new PageInfo<User>(userList);
		request.setAttribute("page", page);
		return "user/userList";
	}
}
