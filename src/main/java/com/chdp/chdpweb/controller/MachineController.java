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
		if (machineService.doesMachineExist(machine)){
			request.setAttribute("errorMsg", "此机器名已经存在，请更改名称后重新添加！");
		}else{
			machine.setType(Integer.parseInt(request.getParameter("type")));
			if (machineService.addMachine(machine)){
				request.setAttribute("successMsg", "添加机器成功！");
			}else{
				request.setAttribute("errorMsg", "添加机器失败，请稍后重试！");
						
		    }
        }
		
		request.setAttribute("machineAdd", machine);
		return "machine/addMachine";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, 
			@RequestParam(name = "machineId") Integer machineId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum){
		request.setAttribute("nav", "机器管理");
		
		if (machineId == null){
			request.setAttribute("errorMsg", "未知的机器ID！");
		}else{
			if (machineService.isMachineInUse(machineId)) {
				request.setAttribute("errorMsg", "当前有处方只在使用此机器，请稍后删除！");
			}else{
				if (machineService.deleteMachine(machineId)){
					request.setAttribute("successMsg", "删除机器成功");
				}else{
					request.setAttribute("errorMsg", "删除机器失败，请稍后重试！");
				}
			}
		}
		
		List<Machine> machineList = machineService.getMachineList(pageNum);
		request.setAttribute("machineList", machineList);
		PageInfo<Machine> page = new PageInfo<Machine>(machineList);
		request.setAttribute("page", page);

		return "machine/machineList";
	}
	
}
