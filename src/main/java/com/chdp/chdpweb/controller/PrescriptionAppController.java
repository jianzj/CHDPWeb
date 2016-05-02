package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.service.PrescriptionService;

@Controller
@RequestMapping("/app/prescription")
public class PrescriptionAppController {

	@Autowired
	private PrescriptionService prsService;

	@RequestMapping(value = "/getPrescription", method = RequestMethod.POST)
	@ResponseBody
	public Prescription receiveModify(HttpServletRequest request) {
		String uuid = request.getParameter("uuid");
		if (uuid == null) {
			return null;
		} else {
			return prsService.getPrescriptionByUuid(uuid);
		}
	}
}
