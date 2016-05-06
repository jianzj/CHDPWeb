package com.chdp.chdpweb.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.Process;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.dao.OrderDao;
import com.chdp.chdpweb.dao.PrescriptionDao;
import com.github.pagehelper.PageHelper;

@Repository
public class OrderService {

    @Autowired
    private OrderDao orderDao;
    
    @Autowired
    private PrescriptionDao prsDao;

    public Order getOrder(int id) {
        try {
            return orderDao.getOrderById(id);
        } catch (Exception e) {
            return null;
        }
    }

    public Order getOrderByUuid(String uuid) {
        try {
            return orderDao.getOrderByUuid(uuid);
        } catch (Exception e) {
            return null;
        }
    }
    
    public AppResult finishOrder(int orderId) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        AppResult result = new AppResult();
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = df.format(new Date());
            User user = (User) SecurityUtils.getSubject().getSession().getAttribute("user");
            Order order = new Order();
            order.setId(orderId);
            order.setOutbound_time(currentTime);
            order.setOutbound_user_id(user.getId());
            order.setStatus(Constants.ORDER_FINISH);
            orderDao.updateOutboundAction(order);

            try {
                prsDao.finishPrescription(orderId, currentTime);
            } catch (Exception e) {
            	transactionManager.rollback(status);
                result.setErrorMsg("更新处方信息失败");
                result.setSuccess(false);

                return result;
            }
        } catch (Exception e) {
        	transactionManager.rollback(status);
            result.setErrorMsg("更新出库单失败");
            result.setSuccess(false);

            return result;
        }

        transactionManager.commit(status);
        result.setSuccess(true);
        return result;
    } 
    
    public boolean createFullOrder(Order newOrder){
    	try{
    		orderDao.createFullOrder(newOrder);
    		return true;
    	}catch (Exception e){
    		return false;
    	}
    }
    
    public int getOrderIdByUuid(String uuid){
    	try{
    		return orderDao.getOrderIdByUuid(uuid);
    	} catch (Exception e){
    		return -1;
    	}
    }
    
    public List<Order> listOrderFinished(String hospital, String start, String end){
    	try{
    		if (hospital.equals("ALL")){
    			return orderDao.listOrderAllHospital(start, end, Constants.ORDER_FINISH);
    		}else{
    			return orderDao.listOrder(hospital, start, end, Constants.ORDER_FINISH);
    		}
    	}catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
    
    public List<Order> listOrderFinished(String hospital, String start, String end, int pageNum){
    	PageHelper.startPage(pageNum, Constants.PAGE_SIZE);
    	try{
    		if (hospital.equals("ALL")){
    			return orderDao.listOrderAllHospital(start, end, Constants.ORDER_FINISH);
    		}else{
    			return orderDao.listOrder(hospital, start, end, Constants.ORDER_FINISH);
    		}
    	}catch (Exception e){
    		return new ArrayList<Order>();
    	}
    }
    
    public int countPrsNumInOrder(int orderId){
    	try{
    		return orderDao.countPrsNumInOrder(orderId);
    	} catch (Exception e){
    		return 0;
    	}
    }
}
