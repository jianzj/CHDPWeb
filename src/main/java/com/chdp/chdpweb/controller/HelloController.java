package com.chdp.chdpweb.controller;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class HelloController {
	@RequiresRoles("ADMIN")
	@RequestMapping("/hello")
	public String hello() {
		return "hello";
	}
}
