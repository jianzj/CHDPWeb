package com.chdp.chdpweb.dao;

import java.util.List;

import com.chdp.chdpweb.bean.Process;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface ProcessDao {

	@Insert("insert process(process_type, begin, user_id, prescription_id, previous_process_id) "
			+ "values(#{proc.process_type}, #{proc.begin}, #{proc.user_id}, #{proc.prescription_id}, #{proc.previous_process_id})")
	@Options(useGeneratedKeys = true, keyProperty = "proc.id")
	int createProcess(@Param("proc") Process proc);
	
	@Insert("insert process(process_type, begin, user_id, prescription_id, previous_process_id, machine_id) "
			+ "values(#{proc.process_type}, #{proc.begin}, #{proc.user_id}, #{proc.prescription_id}, #{proc.previous_process_id}, #{proc.machine_id})")
	@Options(useGeneratedKeys = true, keyProperty = "proc.id")
	int createProcessWithMachine(@Param("proc") Process proc);

	@Update("update process set machine_id = #{proc.machine_id} where id = #{proc.id}")
	int addMachineToProcess(@Param("proc") Process proc);

	@Update("update process set begin = #{proc.begin}, finish = #{proc.finish} where id = #{proc.id}")
	int refreshProcessTime(@Param("proc") Process proc);

	@Update("update process set error_type = #{proc.error_type}, error_msg = #{proc.error_msg} where id = #{proc.id}")
	int addErrorMsgToProcess(@Param("proc") Process proc);

	@Select("select * from process where prescription_id = #{prs_id} order by begin desc")
	List<Process> getProcessesByPrsID(@Param("prs_id") int prs_id);

	@Select("select id from process where process_type = #{process.process_type} and "
			+ "user_id = #{process.user_id} and prescription_id = #{process.prescription_id} and previous_process_id = #{process.previous_process_id}")
	int getProcessIDwithProcess(@Param("process") Process process);

	@Delete("delete from process where id = #{processId}")
	int deleteProcess(@Param("processId") int processId);

	@Select("select p.*, u.name as user_name, m.name as machine_name from user as u, process as p left join machine as m on p.machine_id = m.id where p.id = #{id} and p.user_id = u.id")
	Process getProcessesById(@Param("id") int id);

	@Select("select p.*, u.name as user_name, m.name as machine_name from user as u, process as p left join machine as m on p.machine_id = m.id where p.id = (select process_id from prescription where id = #{id}) and p.user_id = u.id")
	Process getPrescriptionPresentProcess(@Param("id") int id);

	@Update("update process set finish = #{finish}, user_id = #{user_id} where id = #{id}")
	int finishProcess(@Param("id") int id, @Param("finish") String finish, @Param("user_id") int user_id);

	@Update("update process set finish = #{finish}, user_id = #{user_id}, machine_id = #{machine_id} where id = #{id}")
	int finishProcessWithMachine(@Param("id") int id, @Param("finish") String finish, @Param("user_id") int user_id,
			@Param("machine_id") int machine_id);

	@Update("update process set begin = #{begin}, user_id = #{user_id} where id = #{id}")
	int startProcess(@Param("id") int id, @Param("begin") String begin, @Param("user_id") int user_id);

	@Update("update process set middle = #{middle} where id = #{id}")
	int middleProcess(@Param("id") int id, @Param("middle") String middle);

	@Update("update process set begin = #{begin}, user_id = #{user_id}, machine_id = #{machine_id} where id = #{id}")
	int startProcessWithMachine(@Param("id") int id, @Param("begin") String begin, @Param("user_id") int user_id, @Param("machine_id") int machine_id);

	@Update("update process set finish = #{finish}, user_id = #{user_id},error_type = #{type}, error_msg = #{reason} where id = #{id}")
	int cancelProcess(@Param("id") int id, @Param("finish") String finish, @Param("user_id") int user_id,
			@Param("type") int type, @Param("reason") String reason);
	
	@Update("update process set finish = #{finish}, user_id = #{user_id},error_type = #{type}, error_msg = #{reason}, machine_id = #{machine_id} where id = #{id}")
	int cancelProcessWithMachine(@Param("id") int id, @Param("finish") String finish, @Param("user_id") int user_id,
			@Param("type") int type, @Param("reason") String reason, @Param("machine_id") int machine_id);
	
	@Select("select max(id) from process where process_type = #{process} and prescription_id = #{prsId}")
	int getProcIdwithPrsandStatus(@Param("prsId") int prsId, @Param("process") int process);
	
	// Add new selection for Order and Shipping process
	@Select("select * from process where id = " +
	            "(select max(id) from process where prescription_id = #{prsId} and process_type = #{process})")
	Process getProcesswithPrsIdandProcess(@Param("prsId") int prsId, @Param("process") int process);
}
