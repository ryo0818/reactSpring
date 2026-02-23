package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyTeamInfoRepository {

	// チームコード存在チェック
	int checkTeamCode(@Param("userCompanyCode") String userCompanyCode, @Param("userTeamCode") String userTeamCode);



}
