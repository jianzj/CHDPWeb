package com.chdp.chdpweb.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.AppResult;
import com.chdp.chdpweb.bean.Order;
import com.chdp.chdpweb.bean.Prescription;
import com.chdp.chdpweb.bean.Process;
import com.chdp.chdpweb.bean.User;
import com.chdp.chdpweb.dao.OrderDao;
import com.chdp.chdpweb.dao.PrescriptionDao;

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

}
