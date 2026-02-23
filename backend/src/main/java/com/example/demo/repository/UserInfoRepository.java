package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.UserInfoEntity;

@Mapper
public interface UserInfoRepository {

	// ログインユーザー取得
	UserInfoEntity getUserInfo(@Param("userId") String userId,
			@Param("userEmailAddres") String userEmailAddres);

	// 登録
	int insertUser(UserInfoEntity user);
	
}
