package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresRoles;
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

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "用户管理");
		List<User> userList = userService.getUserList(pageNum);
		request.setAttribute("userList", userList);
		PageInfo<User> page = new PageInfo<User>(userList);
		request.setAttribute("page", page);
		return "user/userList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, @RequestParam(name = "userId") Integer userId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "用户管理");

		if (userId == null)
			request.setAttribute("errorMsg", "未知的用户ID");
		else {
			if (userId == ((User) SecurityUtils.getSubject().getSession().getAttribute("user")).getId())
				request.setAttribute("errorMsg", "不允许删除当前登录用户");
			else {
				if (userService.deleteUser(userId))
					request.setAttribute("successMsg", "删除用户成功");
				else
					request.setAttribute("errorMsg", "删除用户失败");
			}
		}

		List<User> userList = userService.getUserList(pageNum);
		request.setAttribute("userList", userList);
		PageInfo<User> page = new PageInfo<User>(userList);
		request.setAttribute("page", page);

		return "user/userList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/resetPassword")
	public String resetPassword(HttpServletRequest request, @RequestParam(name = "userId") Integer userId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "用户管理");

		if (userId == null)
			request.setAttribute("errorMsg", "未知的用户ID");
		else {
			if (userService.resetUserPassword(userId))
				request.setAttribute("successMsg", "重置用户密码成功");
			else
				request.setAttribute("errorMsg", "重置用户密码失败");
		}

		List<User> userList = userService.getUserList(pageNum);
		request.setAttribute("userList", userList);
		PageInfo<User> page = new PageInfo<User>(userList);
		request.setAttribute("page", page);

		return "user/userList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		request.setAttribute("nav", "用户管理");
		return "user/addUser";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, User user) {
		request.setAttribute("nav", "用户管理");
		String rePassword = request.getParameter("rePassword");
		user.setAuthority((request.getParameter("authority-1024") == null ? 0 : 1024)
				| (request.getParameter("authority-512") == null ? 0 : 512)
				| (request.getParameter("authority-256") == null ? 0 : 256)
				| (request.getParameter("authority-128") == null ? 0 : 128)
				| (request.getParameter("authority-64") == null ? 0 : 64)
				| (request.getParameter("authority-32") == null ? 0 : 32)
				| (request.getParameter("authority-16") == null ? 0 : 16)
				| (request.getParameter("authority-8") == null ? 0 : 8)
				| (request.getParameter("authority-4") == null ? 0 : 4)
				| (request.getParameter("authority-2") == null ? 0 : 2)
				| (request.getParameter("authority-1") == null ? 0 : 1));

		if (!user.getPassword().equals(rePassword)) {
			request.setAttribute("errorMsg", "两次密码输入不同");
		} else {
			if (userService.getUser(user.getUsercode()) != null) {
				request.setAttribute("errorMsg", "用户已存在，请修改工号");
			} else {
				if (userService.addUser(user))
					request.setAttribute("successMsg", "新建用户成功");
				else
					request.setAttribute("errorMsg", "新建用户失败");
			}
		}
		request.setAttribute("userAdd", user);
		return "user/addUser";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/modify", method = RequestMethod.GET)
	public String modify(HttpServletRequest request, @RequestParam(name = "userId") Integer userId) {
		request.setAttribute("nav", "用户管理");
		if (userId == null)
			request.setAttribute("errorMsg", "未知的用户ID");
		else
			request.setAttribute("userModify", userService.getUserById(userId));

		return "user/modifyUser";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/modify", method = RequestMethod.POST)
	public String modifyPost(HttpServletRequest request, @RequestParam(name = "userId") Integer userId) {
		request.setAttribute("nav", "用户管理");
		if (userId == null)
			request.setAttribute("errorMsg", "未知的用户ID");
		else {
			User user = userService.getUserById(userId);
			user.setName(request.getParameter("name"));
			user.setAuthority((request.getParameter("authority-1024") == null ? 0 : 1024)
					| (request.getParameter("authority-512") == null ? 0 : 512)
					| (request.getParameter("authority-256") == null ? 0 : 256)
					| (request.getParameter("authority-128") == null ? 0 : 128)
					| (request.getParameter("authority-64") == null ? 0 : 64)
					| (request.getParameter("authority-32") == null ? 0 : 32)
					| (request.getParameter("authority-16") == null ? 0 : 16)
					| (request.getParameter("authority-8") == null ? 0 : 8)
					| (request.getParameter("authority-4") == null ? 0 : 4)
					| (request.getParameter("authority-2") == null ? 0 : 2)
					| (request.getParameter("authority-1") == null ? 0 : 1));

			if (userService.updateUser(user))
				request.setAttribute("successMsg", "用户修改成功");
			else
				request.setAttribute("errorMsg", "用户修改失败");

			request.setAttribute("userModify", user);
		}
		return "user/modifyUser";
	}
}
