package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginRepository {

	// 会社コード確認
	String cheakUserLogin(@Param("userId") String userId,
		@Param("email") String email);

}
