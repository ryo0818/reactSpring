package com.example.demo.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserLoginRepository {

	// 会社コード確認
	String cheakUserLogin(@Param("myCompanyCode") String myCompanyCode,
		@Param("mailAdder") String mailAdder);

}
