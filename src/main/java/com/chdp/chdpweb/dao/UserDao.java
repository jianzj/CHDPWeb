package com.chdp.chdpweb.dao;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.chdp.chdpweb.bean.User;

@Repository
public interface UserDao {
	@Select("select * from user where usercode = #{usercode}")
	User getUser(@Param("usercode") String usercode);
}
