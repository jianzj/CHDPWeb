package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chdp.chdpweb.service.UserService;

@Controller
@RequestMapping("/machine")
public class MachineController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/addMachine", method = RequestMethod.POST)
	public String addMachine(HttpServletRequest request){
		
		
		
		
		return "machine/machineList";
	}

}
