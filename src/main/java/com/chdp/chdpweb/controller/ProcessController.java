package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chdp.chdpweb.service.ProcessService;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private ProcessService proService;

	@RequestMapping(value = "/getProcess", method = RequestMethod.POST)
	public com.chdp.chdpweb.bean.Process getProcess(HttpServletRequest request) {
		String id = request.getParameter("id");
		if (id == null) {
			return null;
		} else {
			return proService.getProcessById(Integer.parseInt(id));
		}
	}
}
