package com.chdp.chdpweb.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.chdp.chdpweb.Constants;
import com.chdp.chdpweb.bean.Prescription;

public interface PrescriptionDao {

	@Insert("insert prescription(uuid, outer_id, hospital_id, patient_name, sex, packet_num, price, create_time, class_of_medicines, need_decoct_first, "
			+ "need_decoct_later, need_wrapped_decoct, need_take_drenched, need_melt, need_decoct_alone, process, process_id) "
			+ "values(#{prs.uuid}, #{prs.outer_id}, #{prs.hospital_id}, #{prs.patient_name}, #{prs.sex}, #{prs.packet_num}, #{prs.price}, #{prs.create_time}, "
			+ "#{prs.class_of_medicines}, #{prs.need_decoct_first}, #{prs.need_decoct_later}, #{prs.need_wrapped_decoct}, #{prs.need_take_drenched}, "
			+ "#{prs.need_melt}, #{prs.need_decoct_alone}, #{prs.process}, #{prs.process_id})")
	@Options(useGeneratedKeys = true, keyProperty = "prs.id")
	int createPrescription(@Param("prs") Prescription prs);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.id = #{id} and p.hospital_id = h.id")
	Prescription getPrescriptionByID(@Param("id") int id);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.uuid = #{uuid} and p.hospital_id = h.id")
	Prescription getPrescriptionByUUID(@Param("uuid") String uuid);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = 8 and p.hospital_id = h.id and p.process_id in ("
			+ "select pr.id from process as pr, machine as m where "
			+ "pr.process_type = 8 and pr.finish is null and pr.machine_id = m.id and m.uuid = #{uuid}) limit 1")
	Prescription getPrescriptionByPourMachineUuid(@Param("uuid") String uuid);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where "
			+ "p.process = #{process} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsByProcess(@Param("process") int process);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h, process as present, process as previous where "
			+ "p.process = " + Constants.DECOCT
			+ " and p.process_id = present.id and present.previous_process_id = previous.id and previous.finish is null and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsOfSoak();
	
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h, process as present, process as previous where "
			+ "p.process = " + Constants.DECOCT
			+ " and p.process_id = present.id and present.previous_process_id = previous.id and (previous.finish is not null or present.begin is not null) and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsOfDecoct();

	// Used to find out finished prescriptions by hospital and time.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where "
			+ "p.process = #{process} and p.hospital_id = h.id and p.create_time >= #{start} and p.finish_time <= #{end} order by p.create_time desc")
	List<Prescription> getPrescriptionsByProcessAndTime(@Param("process") int process, @Param("start") String start,
			@Param("end") String end);

	// Used to find out finished prescriptions by hospital and time.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and h.id = #{hospitalId} and p.hospital_id = h.id and p.create_time >= #{start} and p.finish_time <= #{end} order by p.create_time desc")
	List<Prescription> getPrescriptionsByParamswithTime(@Param("process") int process,
			@Param("hospitalId") int hospitalId, @Param("start") String start, @Param("end") String end);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where "
			+ "p.process = #{process} and h.id = #{hospitalId} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsByParams(@Param("process") int process, @Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h, process as present, process as previous where "
			+ "p.process = " + Constants.DECOCT
			+ " and p.process_id = present.id and present.previous_process_id = previous.id and previous.finish is null and h.id = #{hospitalId} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsByParamsOfSoak(@Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h, process as present, process as previous where "
			+ "p.process = " + Constants.DECOCT
			+ " and p.process_id = present.id and present.previous_process_id = previous.id and (previous.finish is not null or present.begin is not null) and h.id = #{hospitalId} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsByParamsOfDecoct(@Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where h.id = #{hospitalId} and h.id = p.hospital_id and p.process < 11 order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionByHospitalId(@Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process < 11 and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrescriptionsUnfinished();

	// 获取在一段时间内指定一家医院完成的处方
	@Select("select count(*) from prescription as p, hospital as h where h.id = #{hospitalId} and p.hospital_id = h.id and p.process = #{process} "
			+ "and p.create_time >= #{start} and p.finish_time <= #{end}")
	int countPrsNumForHospital(@Param("hospitalId") int hospitalId, @Param("process") int process,
			@Param("start") String start, @Param("end") String end);

	// Cut Lines

	@Select("select * from prescription where hospital_id = #{hospital_id} and process = #{process} order by create_time desc")
	List<Prescription> getPrescriptionByHospitalwithProcess(@Param("hospital_id") int hospital_id,
			@Param("process") int process);

	@Update("update prescription set order_id = #{prs.order_id} where id = #{prs.id}")
	int updatePrescriptionOrderId(@Param("prs") Prescription prs);

	@Update("update prescription set process = #{prs.process}, process_id = #{prs.process_id} where id = #{prs.id}")
	int updatePrescriptionProcess(@Param("prs") Prescription prs);

	@Update("update prescription set outer_id = #{prs.outer_id}, hospital_id = #{prs.hospital_id}, patient_name = #{prs.patient_name}, "
			+ "sex = #{prs.sex}, packet_num = #{prs.packet_num}, price = #{prs.price} where id = #{prs.id}")
	int updatePrescriptionByPhase1(@Param("prs") Prescription prs);

	// This method is used update all sections about if these methods are
	// needed, such as need_decoct first, or need_decoct_later.
	@Update("update prescription set class_of_medicines = #{prs.class_of_medicines}, need_decoct_first = #{prs.need_decoct_first}, "
			+ "decoct_first_list = #{prs.decoct_first_list}, need_decoct_later = #{prs.need_decoct_later}, decoct_later_list = #{prs.decoct_later_list}, "
			+ "need_wrapped_decoct = #{prs.need_wrapped_decoct}, wrapped_decoct_list = #{prs.wrapped_decoct_list}, need_take_drenched = #{prs.need_take_drenched}, "
			+ "take_drenched_list = #{prs.take_drenched_list}, need_melt = #{prs.need_melt}, melt_list = #{prs.melt_list}, need_decoct_alone = #{prs.need_decoct_alone}, "
			+ "decoct_alone_list = #{prs.decoct_alone_list} where uuid = #{prs.uuid}")
	int updatePrescriptionMethods(@Param("prs") Prescription prs);

	@Update("update prescription set finish_time = #{prs.finish_time} where uuid = #{prs.uuid}")
	int updatePrescriptionFinishTime(@Param("prs") Prescription prs);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.id = (select max(id) from prescription) and p.hospital_id = h.id")
	Prescription getLastestPrescription();

	@Select("select count(*) from prescription as p, hospital as h where h.id = #{prs.hospital_id} and p.outer_id = #{prs.outer_id} and p.hospital_id = h.id")
	int countPrescriptionWithHospitalInfo(@Param("prs") Prescription prs);

	@Delete("delete from prescription where id = #{prsId}")
	int deletePrescription(@Param("prsId") int prsId);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.process_id = -1 and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> getPrsListWithProcess_Ship(@Param("process") int process);

	// New added
	// Used for receiveList/ to get PrsList with Process type. No user_name
	// included.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrsListWithProcess(@Param("process") int process);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.id = #{id} and p.hospital_id = h.id")
	Prescription getPrswithIdNoUser(@Param("id") int id);

	// Used for receiveList/ to get PrsList with Process type. No user_name
	// included.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.process_id = -1 and h.id = #{hospitalId} and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> listShipPrescription(@Param("process") int process, @Param("hospitalId") int hospitalId);

	// Used for receiveList/ to get PrsList with Process type. No user_name
	// included.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and h.id = #{hospitalId} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrsListWithProAndHospital(@Param("process") int process, @Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, process as present, hospital as h " + "where "
			+ "present.id = p.process_id and ((p.process = " + Constants.DECOCT
			+ " and present.begin is not null) or (p.process > " + Constants.DECOCT + " and p.process <= "
			+ Constants.PACKAGE
			+ ")) and h.id = #{hospitalId} and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> listPackagePrsWithHospital(@Param("hospitalId") int hospitalId);

	@Select("select p.*, h.name as hospital_name from prescription as p, process as present, hospital as h " + "where "
			+ "present.id = p.process_id and ((p.process = " + Constants.DECOCT
			+ " and present.begin is not null) or (p.process > " + Constants.DECOCT + " and p.process <= "
			+ Constants.PACKAGE + ")) and p.hospital_id = h.id order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> listPackagePrs();

	@Select("select h.id from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.hospital_id = h.id")
	List<Integer> listInProgressHospitalwithProcess(@Param("process") int process);

	@Update("update prescription set finish_time = #{currentTime}, process = " + Constants.FINISH + " where process = "
			+ Constants.SHIP + " and process_id = #{orderId}")
	int finishPrescription(@Param("orderId") int orderId, @Param("currentTime") String currentTime);

	@Update("update prescription set finish_time = #{prs.finish_time}, process = #{prs.process}, process_id = #{prs.process_id} where id = #{prs.id}")
	int markPrsFinished(@Param("prs") Prescription prs);

	// 用户维度统计:计算处理的Prs, Process < 10;
	@Select("select count(distinct p.id) from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.user_id = u.id "
			+ "and pro.prescription_id = p.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	int countPrsDealByUser_NoShip(@Param("userId") int userId, @Param("start") String start, @Param("end") String end);

	@Select("select distinct p.id from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.user_id = u.id "
			+ "and pro.prescription_id = p.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Integer> listDealPrsIdByUser_NoShip(@Param("userId") int userId, @Param("start") String start,
			@Param("end") String end);

	// 用户维度统计:计算处理的Prs, Process < 10;
	@Select("select count(distinct p.id) from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.user_id = u.id "
			+ "and pro.prescription_id = p.id and pro.process_type = #{process_type} and p.create_time >= #{start} and p.finish_time <= #{end}")
	int countPrsDealByUserAndProcess_NoShip(@Param("userId") int userId, @Param("process_type") int process_type,
			@Param("start") String start, @Param("end") String end);

	// 用户维度统计:计算处理的Prs, Process < 10;
	@Select("select distinct p.id from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.user_id = u.id "
			+ "and pro.prescription_id = p.id and pro.process_type = #{process_type} and p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Integer> listDealPrsIdByUserAndProcess_NoShip(@Param("userId") int userId,
			@Param("process_type") int process_type, @Param("start") String start, @Param("end") String end);

	@Select("select count(distinct p.id) from prescription as p, user as u, CHDP.order as o where u.id = #{userId} and "
			+ "(o.create_user_id = u.id or o.outbound_user_id = u.id) and p.process = 11 and p.process_id = o.id and "
			+ "p.create_time >= #{start} and p.finish_time <= #{end}")
	int countPrsDealByUser_Ship(@Param("userId") int userId, @Param("start") String start, @Param("end") String end);

	@Select("select distinct p.id from prescription as p, user as u, CHDP.order as o where u.id = #{userId} and "
			+ "(o.create_user_id = u.id or o.outbound_user_id = u.id) and p.process = 11 and p.process_id = o.id and "
			+ "p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Integer> listDealPrsIdByUserr_Ship(@Param("userId") int userId, @Param("start") String start,
			@Param("end") String end);

	// 用户维度统计:PrsList
	@Select("select p.*, h.name as hospital_name from prescription as p, process as pro, user as u, hospital as h where p.process = 11 and u.id = #{userId} and pro.process_type = #{process_type} "
			+ "and p.hospital_id = h.id and pro.prescription_id = p.id and pro.user_id = u.id and p.create_time >= #{start} and p.finish_time <= #{end} order by p.create_time desc")
	List<Prescription> listPrsByUser(@Param("userId") int userId, @Param("process_type") int process_type,
			@Param("start") String start, @Param("end") String end);

	// Errors
	@Select("select count(distinct p1.prescription_id) from process as p1, process as p2 where p2.user_id = #{userId} and p1.process_type = #{process_type} "
			+ "p1.previous_process_id = p2.id and p1.error_type != 0 and p1.begin != null and p1.begin >= #{start} and p1.finish != null and p1.finish <= #{end} group by p2.user_id")
	Integer countProcsErrorByUse_Process(@Param("process_type") int process_type, @Param("userId") int userId,
			@Param("start") String start, @Param("end") String end);

	@Select("select * from prescription where hospital_id = #{hospital_id} order by create_time desc")
	List<Prescription> getPrescriptionByHospital(@Param("hospital_id") int hospital_id);

	@Update("update prescription set class_of_medicines = #{prs.class_of_medicines}, process = #{prs.process}, process_id = #{prs.process_id}, "
			+ "need_decoct_first = #{prs.need_decoct_first}, decoct_first_list = #{prs.decoct_first_list, jdbcType=LONGVARCHAR},"
			+ "need_decoct_later = #{prs.need_decoct_later}, decoct_later_list = #{prs.decoct_later_list, jdbcType=LONGVARCHAR},"
			+ "need_wrapped_decoct = #{prs.need_wrapped_decoct}, wrapped_decoct_list = #{prs.wrapped_decoct_list, jdbcType=LONGVARCHAR},"
			+ "need_take_drenched = #{prs.need_take_drenched}, take_drenched_list = #{prs.take_drenched_list, jdbcType=LONGVARCHAR},"
			+ "need_melt = #{prs.need_melt}, melt_list = #{prs.melt_list, jdbcType=LONGVARCHAR},"
			+ "need_decoct_alone = #{prs.need_decoct_alone}, decoct_alone_list = #{prs.decoct_alone_list, jdbcType=LONGVARCHAR}"
			+ " where id = #{prs.id}")
	int updatePrescription(@Param("prs") Prescription prs);

	/** -------------------------------------------------------------------- **/
	// List all finished prescriptions by orderId
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h  where p.process = "
			+ Constants.FINISH + " and "
			+ "h.id = #{hospitalId} and p.process_id = #{orderId} and p.hospital_id = h.id "
			+ "and create_time >= #{startTime} and finish_time <= #{endTime} order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrsListByOrderId(@Param("orderId") int orderId, @Param("hospitalId") int hospitalId,
			@Param("startTime") String startTime, @Param("endTime") String endTime);

	// List all unfinished prescriptions by orderId
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h  where p.process = "
			+ Constants.SHIP + " and "
			+ "p.process_id = #{orderId} and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> getPrsListByOrderIdUnfinished(@Param("orderId") int orderId);

	// List all finished prescriptions where one user worked for it, checked for
	// process table.
	@Select("select distinct p.*, h.name as hospital_name from prescription as p, process as pro, hospital as h where "
			+ "p.process = " + Constants.FINISH + " and pro.user_id = #{userId} and pro.prescription_id = p.id "
			+ "and p.hospital_id = h.id and p.create_time >= #{startTime} and p.finish_time <= #{endTime} order by p.create_time desc")
	List<Prescription> getPrsListFromProcessByUserId(@Param("userId") int userId, @Param("startTime") String startTime,
			@Param("endTime") String endTime);

	// List all finished prescriptions where one user worked for it, checked for
	// order table.
	@Select("select distinct p.*, h.name as hospital_name from prescription as p, CHDP.order as o, hospital as h where "
			+ "p.process = " + Constants.FINISH
			+ " and (o.create_user_id = #{userId} or o.outbound_user_id = #{userId}) "
			+ "and p.process_id = o.id and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> getPrsListFromOrderByUserId(@Param("userId") int userId, @Param("startTime") String startTime,
			@Param("endTime") String endTime);

	// 用户维度统计:计算出错的Process
	@Select("select count(1) from prescription where id in ( select distinct p1.prescription_id from process as p1, process as p2 where p2.user_id = #{userId} and "
			+ "p1.previous_process_id = p2.id and p1.error_type != 0 and p2.begin >= #{start} and p2.finish <= #{end} group by p2.user_id) and process="
			+ Constants.FINISH)
	Integer getErrorProcessByUserId(@Param("userId") int userId, @Param("start") String start,
			@Param("end") String end);

	// 允许处方创建过程中，同一天不允许出现重复编号的处方，不同一天，可以出现同一编号的处方
	@Select("select count(*) from prescription as p, hospital as h where h.id = #{prs.hospital_id} and p.outer_id = #{prs.outer_id} and p.hospital_id = h.id and "
			+ "p.create_time >= #{start} and p.create_time <= #{end}")
	int countPrescriptionWithHospitalInfo_New(@Param("prs") Prescription prs, @Param("start") String start,
			@Param("end") String end);

	// 获取接方完成且尚在进行中的处方列表
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where "
			+ "p.process >= 2 and p.process < 11 and h.id = #{hospitalId} and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> getPrsForPrintOrderList(@Param("hospitalId") int hospitalId);

	// 获取接方完成且尚在进行中的处方列表, 且尚未打印出库单的列表
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where "
			+ "p.process >= 2 and p.process < 11 and h.id = #{hospitalId} and p.hospital_id = h.id and p.order_id = 0 order by p.hospital_id,p.outer_id + 0 asc")
	List<Prescription> getPrsForPrintOrderListUnprinted(@Param("hospitalId") int hospitalId);

	// 获取已经包含在此出库单中的处方
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h  where p.process >= 2 and p.process < 11 "
			+ "and p.order_id = #{orderId} and p.hospital_id = h.id order by p.create_time desc")
	List<Prescription> getPrsListByOrderIdInProcess(@Param("orderId") int orderId);
}
