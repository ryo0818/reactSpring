package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginRepository {

	// ログインユーザー確認
	String cheakLoginUser(@Param("userId") String userId,
		@Param("email") String email);

	// 会社コード確認
	String cheakCompanyCode(@Param("mycompanycode") String mycompanycode);
}
