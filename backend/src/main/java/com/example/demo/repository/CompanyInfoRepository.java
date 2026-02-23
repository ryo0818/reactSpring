package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CompanyInfoRepository {


	// 会社コード確認
	int checkCompanyCode(@Param("userCompanyCode") String userCompanyCode);
	
}
