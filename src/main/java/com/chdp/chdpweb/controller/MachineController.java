package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Machine;
import com.chdp.chdpweb.common.Utils;
import com.chdp.chdpweb.printer.PrintHelper;
import com.chdp.chdpweb.service.MachineService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/machine")
public class MachineController {

	@Autowired
	private MachineService machineService;

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/list")
	public String list(HttpServletRequest request, @RequestParam(name = "pageNum", defaultValue = "1") int pageNum) {
		request.setAttribute("nav", "机器管理");

		List<Machine> machineList = machineService.getMachineList(pageNum);
		request.setAttribute("machineList", machineList);

		PageInfo<Machine> page = new PageInfo<Machine>(machineList);
		request.setAttribute("page", page);

		return "machine/machineList";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String add(HttpServletRequest request) {
		request.setAttribute("nav", "机器管理");

		List<Machine> pourMachineList = machineService.getMachineListByType(Constants.FILLING_MACHINE);
		if (pourMachineList.size() > 0) {
			request.setAttribute("pourMachineList", pourMachineList);
		}
		return "machine/addMachine";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addPost(HttpServletRequest request, Machine machine) {
		request.setAttribute("nav", "机器管理");

		if (machineService.doesMachineExist(machine)) {
			request.setAttribute("errorMsg", "此机器名已经存在，请更改名称后重新添加！");
		} else {
			if (machine.getType() == 1 && machine.getPour_machine_id() == 0) {
				request.setAttribute("errorMsg", "您未选择关联的灌装机，请重新输入！");
			} else {
				if (machine.getType() == 2)
					machine.setPour_machine_id(0);
				machine.setUuid(Utils.generateUuid());
				if (machineService.addMachine(machine)) {
					request.setAttribute("successMsg", "添加机器成功！");
				} else {
					request.setAttribute("errorMsg", "添加机器失败，请稍后重试！");
				}
			}
		}

		List<Machine> pourMachineList = machineService.getMachineListByType(Constants.FILLING_MACHINE);
		if (pourMachineList.size() > 0) {
			request.setAttribute("pourMachineList", pourMachineList);
		}

		request.setAttribute("machineAdd", machine);
		return "machine/addMachine";
	}

	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/delete")
	public String delete(HttpServletRequest request, @RequestParam(name = "machineId") Integer machineId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, RedirectAttributes redirectAttributes) {
		if (machineId == null) {
			redirectAttributes.addFlashAttribute("errorMsg", "未知的机器ID！");
		} else if (machineService.existRelatedMachines(machineId, Constants.DECOCTION_MACHINE)) {
			redirectAttributes.addFlashAttribute("errorMsg", "此机器为灌装机，已有煎煮机与其关联，请先删除或更改煎煮机关联项！");
		} else {
			if (machineService.isMachineInUse(machineId)) {
				redirectAttributes.addFlashAttribute("errorMsg", "当前有处方正在使用此机器，请稍后删除！");
			} else {
				if (machineService.deleteMachine(machineId)) {
					redirectAttributes.addFlashAttribute("successMsg", "删除机器成功");
				} else {
					redirectAttributes.addFlashAttribute("errorMsg", "删除机器失败，请稍后重试！");
				}
			}
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../machine/list?pageNum=" + pageNum;
	}

	// 打印机器标签
	@RequiresRoles("ADMIN")
	@RequestMapping(value = "/printMachineLabel")
	public String printMachineLabel(HttpServletRequest request, @RequestParam(name = "machineId") Integer machineId,
			@RequestParam(name = "pageNum", defaultValue = "1") int pageNum, RedirectAttributes redirectAttributes) {
		if (machineId == null) {
			redirectAttributes.addFlashAttribute("errorMsg", "未知的机器ID！");
		} else {
			Machine machine = machineService.getMachineById(machineId);
			PrintHelper.startAndSetup();
			PrintHelper.printMachine(machine.getName(), machine.getUuid());
			PrintHelper.close();
			redirectAttributes.addFlashAttribute("successMsg", "打印机器标签成功！");
		}

		return InternalResourceViewResolver.REDIRECT_URL_PREFIX + "../machine/list?pageNum=" + pageNum;
	}
}
