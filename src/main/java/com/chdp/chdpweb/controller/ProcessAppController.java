package com.chdp.chdpweb.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Prescription;
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

	@RequestMapping(value = "/check", method = RequestMethod.POST)
	@ResponseBody
	public AppResult check(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));

		Prescription prs = new Prescription();
		prs.setId(prsId);
		prs.setClass_of_medicines(Integer.parseInt(request.getParameter("type")));
		prs.setNeed_decoct_first(Integer.parseInt(request.getParameter("checkFirst")));
		prs.setDecoct_first_list(request.getParameter("txtFrist"));
		prs.setNeed_decoct_later(Integer.parseInt(request.getParameter("checkLater")));
		prs.setDecoct_later_list(request.getParameter("txtLater"));
		prs.setNeed_wrapped_decoct(Integer.parseInt(request.getParameter("checkWrap")));
		prs.setWrapped_decoct_list(request.getParameter("txtWrap"));
		prs.setNeed_take_drenched(Integer.parseInt(request.getParameter("checkDrink")));
		prs.setTake_drenched_list(request.getParameter("txtDrink"));
		prs.setNeed_melt(Integer.parseInt(request.getParameter("checkMelt")));
		prs.setMelt_list(request.getParameter("txtMelt"));
		prs.setNeed_decoct_alone(Integer.parseInt(request.getParameter("checkAlone")));
		prs.setDecoct_alone_list(request.getParameter("txtAlone"));

		return procService.forwardProcess(prsId, procId, prs);
	}

	@RequestMapping(value = "/checkCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult checkCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.RECEIVE, 1, reason);
	}

	@RequestMapping(value = "/mix", method = RequestMethod.POST)
	@ResponseBody
	public AppResult mix(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		return procService.forwardProcess(prsId, procId, Constants.MIXCHECK, 0);
	}

	@RequestMapping(value = "/mixCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult mixCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.CHECK, 1, reason);
	}

	@RequestMapping(value = "/mixcheck", method = RequestMethod.POST)
	@ResponseBody
	public AppResult mixcheck(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		return procService.forwardProcess(prsId, procId, Constants.SOAK, 0);
	}

	@RequestMapping(value = "/mixcheckCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult mixcheckCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.MIX, 2, reason);
	}

	@RequestMapping(value = "/soak", method = RequestMethod.POST)
	@ResponseBody
	public AppResult soak(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		return procService.forwardProcess(prsId, procId, Constants.DECOCT, 0);
	}

	@RequestMapping(value = "/soakCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult soakCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.MIXCHECK, 1, reason);
	}

	@RequestMapping(value = "/start", method = RequestMethod.POST)
	@ResponseBody
	public AppResult start(HttpServletRequest request) {
		int procId = Integer.parseInt(request.getParameter("procId"));
		int proc = Integer.parseInt(request.getParameter("proc"));

		return procService.startProcess(procId, proc);
	}
	
	@RequestMapping(value = "/checkAndFinish", method = RequestMethod.POST)
    @ResponseBody
    public AppResult checkAndFinish(HttpServletRequest request) {
        int procId = Integer.parseInt(request.getParameter("procId"));

        return procService.checkAndFinish(procId);
    }

	@RequestMapping(value = "/decoct", method = RequestMethod.POST)
	@ResponseBody
	public AppResult decoct(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		int machineId = Integer.parseInt(request.getParameter("machineId"));
		return procService.forwardProcess(prsId, procId, Constants.POUR, machineId);
	}

	@RequestMapping(value = "/decoctCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult decoctCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.SOAK, 1, reason);
	}

	@RequestMapping(value = "/pour", method = RequestMethod.POST)
	@ResponseBody
	public AppResult pour(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		int machineId = Integer.parseInt(request.getParameter("machineId"));
		return procService.forwardProcess(prsId, procId, Constants.CLEAN, machineId);
	}

	@RequestMapping(value = "/pourCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult pourCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.DECOCT, 1, reason);
	}

	@RequestMapping(value = "/clean", method = RequestMethod.POST)
	@ResponseBody
	public AppResult clean(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		return procService.forwardProcess(prsId, procId, Constants.PACKAGE, 0);
	}

	@RequestMapping(value = "/cleanCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult cleanCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.POUR, 1, reason);
	}

	@RequestMapping(value = "/pack", method = RequestMethod.POST)
	@ResponseBody
	public AppResult pack(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		return procService.forwardProcess(prsId, procId, Constants.SHIP, 0);
	}

	@RequestMapping(value = "/packCancel", method = RequestMethod.POST)
	@ResponseBody
	public AppResult packCancel(HttpServletRequest request) {
		int prsId = Integer.parseInt(request.getParameter("prsId"));
		int procId = Integer.parseInt(request.getParameter("procId"));
		String reason = request.getParameter("reason");

		return procService.backwardProcess(prsId, procId, Constants.CLEAN, 2, reason);
	}
}
