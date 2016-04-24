package com.chdp.chdpweb.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.User;

@Repository
public interface UserDao {
	@Select("select * from user where usercode = #{usercode}")
	User getUser(@Param("usercode") String usercode);

	@Update("update user set password = #{user.password} where id = #{user.id}")
	int changePassword(@Param("user") User user);
	
	@Select("select * from user")
	List<User> getUserList();
}
