package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProcessDao {

	@Insert("insert process(process_type, user_id, prescription_id, previous_process_id) " +
	          "values(#{proc.process_type}, #{proc.user_id}, #{proc.prescription_id}, #{proc.previous_process_id})")
	int createProcess(@Param("proc") Process proc);
	
	@Update("update process set machine_id = #{proc.machine_id} where id = #{proc.id}")
	int addMachineToProcess(@Param("proc") Process proc);
	
	@Update("update process set begin = #{proc.begin}, finish = #{proc.finish} where id = #{proc.id}")
	int refreshProcessTime(@Param("proc") Process proc);
	
	@Update("update process set error_type = #{proc.error_type}, error_msg = #{proc.error_msg} where id = #{proc.id}")
	int addErrorMsgToProcess(@Param("proc") Process proc);
	
	@Select("select * from process where prescription_id = #{prs_id}")
	List<Process> getProcessesByPrsID(@Param("prs_id") int prs_id);

	@Select("select count(id) from process where machine_id = #{machine_id} and (process_type = 6 or process_type = 7)")
	int countProcessInMachine(@Param("machine_id") int machine_id);
}
