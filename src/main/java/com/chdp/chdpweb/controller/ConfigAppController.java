package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.Constants;

@Controller
@RequestMapping("/app/config")
public class ConfigAppController {

	@RequestMapping(value = "/getVersion")
	@ResponseBody
	public String getOrder(HttpServletRequest request) {
		return Constants.VERSION;
	}
}
