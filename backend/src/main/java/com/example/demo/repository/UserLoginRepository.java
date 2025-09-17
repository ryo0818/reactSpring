package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.UserInfoEntity;

@Mapper
public interface UserLoginRepository {

	// ログインユーザー取得
	UserInfoEntity getUserInfo(@Param("userId") String userId,
		@Param("userEmailAddres") String userEmailAddres);

	// 会社コード確認
	int checkCompanyCode(@Param("userCompanyCode") String userCompanyCode);
	
	// チームコード存在チェック
	int checkTeamCode(@Param("userCompanyCode") String userCompanyCode,@Param("userTeamCode") String userTeamCode);

	// 登録
	int insertUser(UserInfoEntity user);

}
