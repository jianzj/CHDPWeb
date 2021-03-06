package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.chdp.chdpweb.bean.Order;

public interface OrderDao {
	@Insert("insert CHDP.order(uuid, hospital_id, create_time) values(#{order.uuid}, #{order.hospital_id}, #{order.create_time})")
	int createOrder(@Param("order") Order order);

	@Insert("insert CHDP.order(uuid, hospital_id, create_time, create_user_id, status) "
			+ "values(#{order.uuid}, #{order.hospital_id}, #{order.create_time}, #{order.create_user_id}, #{order.status})")
	@Options(useGeneratedKeys = true, keyProperty = "order.id")
	int createFullOrder(@Param("order") Order order);

	@Select("select id from CHDP.order where uuid = #{orderUuid}")
	int getOrderIdByUuid(@Param("orderUuid") String orderUuid);

	@Update("update CHDP.order set create_time = #{order.create_time}, create_user_id = #{order.create_user_id} "
			+ "where uuid = #{order.uuid}")
	int updateCreateAction(@Param("order") Order order);

	@Update("update CHDP.order set outbound_time = #{order.outbound_time}, outbound_user_id = #{order.outbound_user_id}, "
			+ "status = #{order.status} where id = #{order.id}")
	int updateOutboundAction(@Param("order") Order order);

	@Update("update CHDP.order set status = #{order.status} where uuid = #{order.uuid}")
	int updateOrderStatus(@Param("order") Order order);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.id = #{id} and o.hospital_id = h.id")
	Order getOrderById(@Param("id") int id);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.uuid = #{uuid} and o.hospital_id = h.id")
	Order getOrderByUuid(@Param("uuid") String uuid);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = #{order_status} and "
			+ "h.id = #{hospitalId} and o.hospital_id = h.id and o.create_time >= #{start} and o.create_time <= #{end} order by o.create_time")
	List<Order> listOrder(@Param("hospitalId") int hospitalId, @Param("start") String start, @Param("end") String end,
			@Param("order_status") int order_status);

	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = #{order_status} and "
			+ "o.hospital_id = h.id and o.create_time >= #{start} and o.create_time <= #{end} order by o.create_time")
	List<Order> listOrderAllHospital(@Param("start") String start, @Param("end") String end,
			@Param("order_status") int order_status);

	@Select("select count(*) from CHDP.order as o, prescription as p where o.id = #{orderId} and p.process = 11 and o.status = 2 and p.process_id = o.id")
	int countPrsNumInOrder(@Param("orderId") int orderId);

	@Select("select count(*) from CHDP.order as o, prescription as p where o.id = #{orderId} and p.process_id = o.id")
	int countPrsNumWithOrder(@Param("orderId") int orderId);
	
	/** ---------------------------------------------------**/
	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = 1 and o.hospital_id = h.id order by o.create_time desc")
	public List<Order> getOrderListUnfinished();
	
	@Select("select o.*, h.name as hospital_name from CHDP.order as o, hospital as h where o.status = 1 and h.id = #{hospitalId} and o.hospital_id = h.id order by o.create_time desc")
	public List<Order> getOrderListByHospitalIdUnfinished(@Param("hospitalId") int hospitalId);
}
