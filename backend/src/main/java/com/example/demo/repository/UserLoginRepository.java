package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginRepository {

	// ログインユーザー確認
	String cheackLoginUser(@Param("userId") String userId,
		@Param("email") String email);

	// 会社コード確認
	String cheackCompanyCode(@Param("companycode") String mycompanycode);
}
