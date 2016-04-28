package com.chdp.chdpweb.dao;

import java.util.List;

import com.chdp.chdpweb.bean.Process;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProcessDao {

	@Insert("insert process(process_type, begin, user_id, prescription_id, previous_process_id) " +
	          "values(#{proc.process_type}, #{proc.begin}, #{proc.user_id}, #{proc.prescription_id}, #{proc.previous_process_id})")
	int createProcess(@Param("proc") Process proc);
	
	@Update("update process set machine_id = #{proc.machine_id} where id = #{proc.id}")
	int addMachineToProcess(@Param("proc") Process proc);
	
	@Update("update process set begin = #{proc.begin}, finish = #{proc.finish} where id = #{proc.id}")
	int refreshProcessTime(@Param("proc") Process proc);
	
	@Update("update process set error_type = #{proc.error_type}, error_msg = #{proc.error_msg} where id = #{proc.id}")
	int addErrorMsgToProcess(@Param("proc") Process proc);
	
	//@Select("select count(*) from process where prescription_id = #{prs_id}")
	//int countProcesswithPrsId(@Param("prs_id") int prs_id);
	
	//@Select("select * from process where prescription_id = #{prs_id}")
	//Process getSingleProcessByPrsId(@Param("prs_id") int prs_id);
	
	@Select("select * from process where prescription_id = #{prs_id}")
	List<Process> getProcessesByPrsID(@Param("prs_id") int prs_id);

	@Select("select count(id) from process where machine_id = #{machine_id} and (process_type = 6 or process_type = 7)")
	int countProcessInMachine(@Param("machine_id") int machine_id);
	
	@Select("select id from process where process_type = #{process.process_type} and " +
	            "user_id = #{process.user_id} and prescription_id = #{process.prescription_id} and previous_process_id = #{process.previous_process_id}")
	int getProcessIDwithProcess(@Param("process") Process process);
	
	@Delete("delete from process where id = #{processId}")
	int deleteProcess(@Param("processId") int processId);
	
}
