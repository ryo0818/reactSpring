package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.RegUserEntity;

@Mapper
public interface UserLoginRepository {

	// ログインユーザー取得
	RegUserEntity getUserInfo(@Param("userId") String userId,
		@Param("email") String email);

	// 会社コード確認
	RegUserEntity cheackCompanyCode(@Param("companycode") String companycode);

	// 登録
	int insertUser(RegUserEntity user);
}
