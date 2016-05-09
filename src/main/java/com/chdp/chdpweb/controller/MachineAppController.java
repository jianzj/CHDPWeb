package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.bean.Machine;
import com.chdp.chdpweb.service.MachineService;

@Controller
@RequestMapping("/app/machine")
public class MachineAppController {

	@Autowired
	private MachineService machineService;

	@RequestMapping(value = "/getMachineByUuidAndType", method = RequestMethod.POST)
	@ResponseBody
	public Machine getMachineByUuidAndType(HttpServletRequest request) {
		return machineService.getMachineByUuidAndType(request.getParameter("uuid"),
				Integer.parseInt(request.getParameter("type")));
	}

	@RequestMapping(value = "/getMachineById", method = RequestMethod.POST)
	@ResponseBody
	public Machine getMachineById(HttpServletRequest request) {
		return machineService.getMachineById(Integer.parseInt(request.getParameter("id")));
	}
}
