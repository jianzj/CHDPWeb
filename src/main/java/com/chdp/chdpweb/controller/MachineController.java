package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.chdp.chdpweb.bean.Machine;
import com.chdp.chdpweb.service.MachineService;
import com.github.pagehelper.PageInfo;


@Controller
@RequestMapping("/machine")
public class MachineController {
	
	@Autowired
	private MachineService machineService;
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
		request.setAttribute("nav", "机器管理");
		List<Machine> machineList = machineService.getMachineList(pageNum);
		request.setAttribute("machineList", machineList);
		PageInfo<Machine> page = new PageInfo<Machine>(machineList);
		request.setAttribute("page", page);				
		return "machine/machineList";
	}
	
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request){
		request.setAttribute("nav", "机器管理");
		return "machine/addMachine";
	}
	
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Machine machine){
		request.setAttribute("nav", "机器管理");
		machine.setType(Integer.parseInt(request.getParameter("machine_type")));
		if (machineService.addMachine(machine)){
			request.setAttribute("successMsg", "添加机器成功！");
		}else{
			request.setAttribute("errorMsg", "添加机器失败！");
		}
		
		request.setAttribute("machineAdd", machine);
		return "machine/addMachine";
	}

}
