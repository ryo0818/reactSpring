package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.SalseEntity;

@Mapper
public interface SaleRepository {

	// 営業リスト検索
	List<SalseEntity> getSalesSearch();

	// 営業情報登録
	int insertSalse(SalseEntity salseHistory);

}
