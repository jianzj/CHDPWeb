package com.chdp.chdpweb.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.ibatis.annotations.Param;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Hospital;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.service.HospitalService;
import com.chdp.chdpweb.service.PrescriptionService;
import com.chdp.chdpweb.service.ProcessService;
import com.chdp.chdpweb.bean.Process;

@Controller
@RequestMapping("/process")
public class ProcessController {

	@Autowired
	private ProcessService proService;
	@Autowired
	private PrescriptionService prsService;
	@Autowired
	private HospitalService hospitalService;
	
	@RequiresRoles(value = {"ADMIN", "RECEIVE"}, logical = Logical.OR)
	@RequestMapping(value = "/receiveList", method = RequestMethod.GET)
	public String listPrsInReceiving(HttpServletRequest request){
				
		String hospital = request.getParameter("hospital");
		if (hospital == null || hospital.equals("")){
			hospital = "ALL";
		}
		
		List<Prescription> prsList = null;
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.RECEIVE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.RECEIVE, hospital);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", hospital);
		request.setAttribute("receiveList", prsList);
	 	return "process/receiveList";
	 }
	
	@RequiresRoles(value = {"ADMIN", "PACKAGE"}, logical = Logical.OR)
	@RequestMapping(value = "/packageList", method = RequestMethod.GET)
	public String listPrsInPackaging(HttpServletRequest request){
				
		String hospital = request.getParameter("hospital");
		if (hospital == null || hospital.equals("")){
			hospital = "ALL";
		}
		
		List<Prescription> prsList = null;
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.PACKAGE);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.PACKAGE, hospital);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", hospital);
		request.setAttribute("packageList", prsList);
	 	return "process/packageList";
	 }

	@RequiresRoles(value = {"ADMIN", "SHIP"}, logical = Logical.OR)
	@RequestMapping(value = "/shipList", method = RequestMethod.GET)
	public String listPrsInShipping(HttpServletRequest request){
				
		String hospital = request.getParameter("hospital");
		if (hospital == null || hospital.equals("")){
			hospital = "ALL";
		}
		
		List<Prescription> prsList = null;
		if (hospital.equals("ALL")){
			prsList = prsService.listPrsWithProcessNoUser(Constants.SHIP);
		}else{
			prsList = prsService.listPrsWithProHospitalNoUser(Constants.SHIP, hospital);
		}
		
		List<Hospital> hospitalList = hospitalService.getHospitalList();
		
		request.setAttribute("hospitalList", hospitalList);
		request.setAttribute("hospital", hospital);
		request.setAttribute("shipList", prsList);
	 	return "process/shipList";
	 }
	
	@RequestMapping(value = "/showAll", method = RequestMethod.GET)
	public String showAllProcesses(HttpServletRequest request, @Param("prsId") Integer prsId){
		
		String from = request.getParameter("from");
		if (from == null){
			from = "currentList";
		}
		
		if (prsId == null){
			request.setAttribute("errorMsg", "未知处方ID！");
			if (from.equals("currentList")){
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/currentList";
			}else{
				return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "/" + "prescription/historyList";
			}
		}
		
		Prescription prs = prsService.getPrescription(prsId);
		List<Process> processList = proService.getProcessChainWithProcessId(prs.getProcess_id());
		Map<Integer, Process> processMap = new HashMap<Integer, Process>();
		int count = 1;
        Iterator<Process> itr = processList.iterator();
		while (itr.hasNext() && count <= processList.size()){
			Process tempProc = itr.next();
			processMap.put(count, tempProc);
			count += 1;
		}
		
		request.setAttribute("processMap", processMap);
		request.setAttribute("prescription", prs);
		
		return "process/processDetail";
	}
}
