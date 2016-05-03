package com.chdp.chdpweb.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

import com.chdp.chdpweb.bean.Prescription;

public interface PrescriptionDao {

	@Insert("insert prescription(uuid, outer_id, hospital_id, patient_name, sex, packet_num, price, create_time, class_of_medicines, need_decoct_first, " +
			  "need_decoct_later, need_wrapped_decoct, need_take_drenched, need_melt, need_decoct_alone, process, process_id) " +
			"values(#{prs.uuid}, #{prs.outer_id}, #{prs.hospital_id}, #{prs.patient_name}, #{prs.sex}, #{prs.packet_num}, #{prs.price}, #{prs.create_time}, " +
			  "#{prs.class_of_medicines}, #{prs.need_decoct_first}, #{prs.need_decoct_later}, #{prs.need_wrapped_decoct}, #{prs.need_take_drenched}, " +
			"#{prs.need_melt}, #{prs.need_decoct_alone}, #{prs.process}, #{prs.process_id})")
	int createPrescription(@Param("prs") Prescription prs);

	@Select("select p.*, h.name as hospital_name, u.name as user_name from prescription as p, hospital as h, " +
	          "user as u, process as pro where p.id = #{id} and p.process_id = pro.id and pro.user_id = u.id")
	Prescription getPrescriptionByID(@Param("id") int id);
	
	@Select("select * from prescription where uuid = #{uuid}")
	Prescription getPrescriptionByUUID(@Param("uuid") String uuid);
	
	@Select("select p.*, h.name as hospital_name, u.name as user_name from prescription as p, hospital as h, " +
	          "user as u, process as pro where p.process = #{process} and p.process_id = pro.id and pro.user_id = u.id")
	List<Prescription> getPrescriptionsByProcess(@Param("process") int process);

	@Select("select p.*, h.name as hospital_name, u.name as user_name from prescription as p, hospital as h, " +
	          "user as u, process as pro where p.process = #{process} and h.name = #{hospitalName} and p.process_id = pro.id and pro.user_id = u.id")
	List<Prescription> getPrescriptionsByParams(@Param("process") int process, @Param("hospitalName") String hospitalName);
	
	@Select("select p.*, h.name as hospital_name, u.name as user_name from prescription as p, hospital as h, " +
	          "user as u, process as pro where h.name = #{hospitalName} and h.id = p.hospital_id " +
			"and p.id = pro.prescription_id and u.id = pro.user_id and pro.id = p.process_id and p.process < 11")
	List<Prescription> getPrescriptionByHospital(@Param("hospitalName") String hospitalName);	
	
	@Select("select p.*, h.name as hospital_name, u.name as user_name from prescription as p, hospital as h, " +
	          "user as u, process as pro where p.hospital_id = h.id " +
			"and p.id = pro.prescription_id and u.id = pro.user_id and pro.id = p.process_id and p.process < 11")
	List<Prescription> getPrescriptionsUnfinished();	
	
	// Cut Lines
	
	@Select("select * from prescription where hospital_id = #{hospital_id} and process = #{process}")
	List<Prescription> getPrescriptionByHospitalwithProcess(@Param("hospital_id") int hospital_id, @Param("process") int process);
		
	@Update("update prescription set process = #{prs.process}, process_id = #{prs.process_id} where uuid = #{prs.uuid}")
	int updatePrescriptionProcess(@Param("prs") Prescription prs);
	
	@Update("update prescription set outer_id = #{prs.outer_id}, hospital_id = #{prs.hospital_id}, patient_name = #{prs.patient_name}, " +
	          "sex = #{prs.sex}, packet_num = #{prs.packet_num}, price = #{prs.price} where id = #{prs.id}")
	int updatePrescriptionByPhase1(@Param("prs") Prescription prs);

	// This method is used update all sections about if these methods are needed, such as need_decoct first, or need_decoct_later.
	@Update("update prescription set class_of_medicines = #{prs.class_of_medicines}, need_decoct_first = #{prs.need_decoct_first}, " +
	          "decoct_first_list = #{prs.decoct_first_list}, need_decoct_later = #{prs.need_decoct_later}, decoct_later_list = #{prs.decoct_later_list}, " +
			"need_wrapped_decoct = #{prs.need_wrapped_decoct}, wrapped_decoct_list = #{prs.wrapped_decoct_list}, need_take_drenched = #{prs.need_take_drenched}, " +
	          "take_drenched_list = #{prs.take_drenched_list}, need_melt = #{prs.need_melt}, melt_list = #{prs.melt_list}, need_decoct_alone = #{prs.need_decoct_alone}, " +
			"decoct_alone_list = #{prs.decoct_alone_list} where uuid = #{prs.uuid}")
	int updatePrescriptionMethods(@Param("prs") Prescription prs);
	
	@Update("update prescription set finish_time = #{prs.finish_time} where uuid = #{prs.uuid}")
	int updatePrescriptionFinishTime(@Param("prs") Prescription prs);
	
	@Select("select p.*, h.name as hospital_name from prescription as p, hospital as h where p.id = (select max(id) from prescription)")
	Prescription getLastestPrescription();
	
	@Select("select count(*) from prescription as p, hospital as h where h.name = #{prs.hospital_name} and p.outer_id = #{prs.outer_id}")
	int countPrescriptionWithHospitalInfo(@Param("prs") Prescription prs);
	
	@Delete("delete from prescription where id = #{prsId}")
	int deletePrescription(@Param("prsId") int prsId);
	
}
