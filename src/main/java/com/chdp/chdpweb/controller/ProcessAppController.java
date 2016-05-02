package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.service.ProcessService;

@Controller
@RequestMapping("/app/process")
public class ProcessAppController {

	@Autowired
	private ProcessService procService;

	@RequestMapping(value = "/getPresentPreviousProcess", method = RequestMethod.POST)
	@ResponseBody
	public List<com.chdp.chdpweb.bean.Process> getPresentPreviousProcess(HttpServletRequest request) {
		int id = Integer.parseInt(request.getParameter("id"));
		return procService.getPresentPreviousProcess(id);
	}

}
