package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.bean.Prescription;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private PrescriptionService prsService;
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/receiveList", method = RequestMethod.GET)
	public String listPrsInReceiving(HttpServletRequest request){
		List<Prescription> prsList = prsService.listPrsWithProcess(Constants.RECEIVE);
		request.setAttribute("receiveList", prsList);
		return "process/receiveList";
	}
	
}
