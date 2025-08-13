package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.RegUserEntity;

@Mapper
public interface UserLoginRepository {

	// ログインユーザー確認
	String cheackLoginUser(@Param("userId") String userId,
		@Param("email") String email);

	// 会社コード確認
	String cheackCompanyCode(@Param("companycode") String mycompanycode);

	// 登録
	int insertUser(RegUserEntity user);
}
