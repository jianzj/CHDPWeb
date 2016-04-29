package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Prescription;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;
	
	@RequiresRoles("RECEIVE")
	@RequestMapping(value = "/receiveList", method = RequestMethod.GET)
	public String listPrsInReceiving(HttpServletRequest request){
		List<Prescription> prsList = prsService.listPrsWithProcess(Constants.RECEIVE);
		request.setAttribute("receiveList", prsList);
		return "process/receiveList";
	}
	
	@RequiresRoles("PACKAGE")
	@RequestMapping(value = "/packageList", method = RequestMethod.GET)
	public String listPrsInPackaging(HttpServletRequest request){
		List<Prescription> prsList = null;
		
		if (request.getParameter("hospital") != null){
			prsList = prsService.listPrsWithParams(Constants.PACKAGE, (String)request.getParameter("hospital"));
			
		}else{
			prsList = prsService.listPrsWithProcess(Constants.PACKAGE);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		request.setAttribute("packageList", prsList);
		return "process/packageList";
	}

	@RequiresRoles("SHIP")
	@RequestMapping(value = "/shipList", method = RequestMethod.GET)
	public String listPrsInShipping(HttpServletRequest request){
		List<Prescription> prsList = null;
		
		if (request.getParameter("hospital") != null){
			prsList = prsService.listPrsWithParams(Constants.SHIP, (String)request.getParameter("hospital"));
			
		}else{
			prsList = prsService.listPrsWithProcess(Constants.SHIP);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		request.setAttribute("hospitalList", hospitalList);
		
		request.setAttribute("shipList", prsList);
		return "process/shipList";
	}
	
}
