package com.chdp.chdpweb.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
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
	int createPrescription(@Param("prs") Prescription prs);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.id = #{id} and p.hospital_id = h.id")
	Prescription getPrescriptionByID(@Param("id") int id);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.uuid = #{uuid} and p.hospital_id = h.id")
	Prescription getPrescriptionByUUID(@Param("uuid") String uuid);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.process = #{process} and p.hospital_id = h.id")
	List<Prescription> getPrescriptionsByProcess(@Param("process") int process);

	// Used to find out finished prescriptions by hospital and time.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.process = #{process} and p.hospital_id = h.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Prescription> getPrescriptionsByProcessAndTime(@Param("process") int process, @Param("start") String start,
			@Param("end") String end);

	// Used to find out finished prescriptions by hospital and time.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and h.id = #{hospitalId} and p.hospital_id = h.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Prescription> getPrescriptionsByParamswithTime(@Param("process") int process,
			@Param("hospitalId") int hospitalId, @Param("start") String start, @Param("end") String end);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.process = #{process} and h.name = #{hospitalName} and p.hospital_id = h.id")
	List<Prescription> getPrescriptionsByParams(@Param("process") int process,
			@Param("hospitalName") String hospitalName);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where h.name = #{hospitalName} and h.id = p.hospital_id and p.process < 11")
	List<Prescription> getPrescriptionByHospitalName(@Param("hospitalName") String hospitalName);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process < 11 and p.hospital_id = h.id ")
	List<Prescription> getPrescriptionsUnfinished();

	// 获取在一段时间内指定一家医院完成的处方
	@Select("select count(*) from prescription as p, hospital as h where h.name = #{hospital} and p.hospital_id = h.id and p.process = #{process} "
			+ "and p.create_time > #{start} and p.finish_time < #{end}")
	int countPrsNumForHospital(@Param("hospital") String hospital, @Param("process") int process,
			@Param("start") String start, @Param("end") String end);

	// Cut Lines

	@Select("select * from prescription where hospital_id = #{hospital_id} and process = #{process}")
	List<Prescription> getPrescriptionByHospitalwithProcess(@Param("hospital_id") int hospital_id,
			@Param("process") int process);

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

	@Select("select count(*) from prescription as p, hospital as h where h.name = #{prs.hospital_name} and p.outer_id = #{prs.outer_id} and p.hospital_id = h.id")
	int countPrescriptionWithHospitalInfo(@Param("prs") Prescription prs);

	@Delete("delete from prescription where id = #{prsId}")
	int deletePrescription(@Param("prsId") int prsId);

	// New added
	// Used for receiveList/ to get PrsList with Process type. No user_name
	// included.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.hospital_id = h.id")
	List<Prescription> getPrsListWithProcess(@Param("process") int process);

	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.id = #{id} and p.hospital_id = h.id")
	Prescription getPrswithIdNoUser(@Param("id") int id);

	// Used for receiveList/ to get PrsList with Process type. No user_name
	// included.
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h "
			+ "where p.process = #{process} and h.name = #{hospitalName} and p.hospital_id = h.id")
	List<Prescription> getPrsListWithProAndHospital(@Param("process") int process,
			@Param("hospitalName") String hospitalName);

	@Select("select h.name from prescription as p, hospital as h "
			+ "where p.process = #{process} and p.hospital_id = h.id")
	List<String> listInProgressHospitalwithProcess(@Param("process") int process);

	@Update("update prescription set finish_time = #{currentTime}, process = " + Constants.FINISH + " where process = "
			+ Constants.SHIP + " and process_id = #{orderId}")
	int finishPrescription(@Param("orderId") int orderId, @Param("currentTime") String currentTime);

	@Update("update prescription set finish_time = #{prs.finish_time}, process = #{prs.process}, process_id = #{prs.process_id} where id = #{prs.id}")
	int markPrsFinished(@Param("prs") Prescription prs);

	// 用户维度统计:计算处理的Prs
	@Select("select count(*) from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.process_type = #{process_type} "
			+ "and pro.prescription_id = p.id and pro.user_id = u.id and p.create_time > #{start} and p.finish_time < #{end}")
	int countPrsDealByUser(@Param("userId") int userId, @Param("process_type") int process_type,
			@Param("start") String start, @Param("end") String end);

	// 用户维度统计:PrsList
	@Select("select p.*, h.name as hospital_name from prescription as p, process as pro, user as u, hospital as h where p.process = 11 and u.id = #{userId} and pro.process_type = #{process_type} "
			+ "and p.hospital_id = h.id and pro.prescription_id = p.id and pro.user_id = u.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	List<Prescription> listPrsByUser(@Param("userId") int userId, @Param("process_type") int process_type,
			@Param("start") String start, @Param("end") String end);

	// 用户维度统计:计算出错的Process
	@Select("select count(*) from prescription as p, process as pro, user as u where p.process = 11 and u.id = #{userId} and pro.error_type > 0 "
			+ "and pro.prescription_id = p.id and pro.user_id = u.id and p.create_time >= #{start} and p.finish_time <= #{end}")
	int countProcsErrorByUser(@Param("userId") int userId, @Param("start") String start, @Param("end") String end);
	
	@Select("select * from prescription where hospital_id = #{hospital_id}")
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
}
