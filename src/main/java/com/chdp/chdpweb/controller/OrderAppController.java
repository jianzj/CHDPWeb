package com.chdp.chdpweb.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.service.OrderService;

@Controller
@RequestMapping("/app/order")
public class OrderAppController {

	@Autowired
	private OrderService orderService;

	@RequestMapping(value = "/getOrder", method = RequestMethod.POST)
	@ResponseBody
	public Order getOrder(HttpServletRequest request) {
		String uuid = request.getParameter("uuid");
		if (uuid == null) {
			return null;
		} else {
			return orderService.getOrderByUuid(uuid);
		}
	}
	
	
	@RequestMapping(value = "/finishOrder", method = RequestMethod.POST)
    @ResponseBody
    public AppResult finishOrder(HttpServletRequest request) {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        return orderService.finishOrder(orderId);
    }
	
	@RequestMapping(value = "/countPrsNumInOrder", method = RequestMethod.POST)
    @ResponseBody
    public int countPrsNumInOrder(HttpServletRequest request) {
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        return orderService.countPrsNumInOrder(orderId);
    }
}
