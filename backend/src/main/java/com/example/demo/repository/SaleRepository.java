package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.SalelistStatusHi;
import com.example.demo.entity.SalesEntity;

@Mapper
public interface SaleRepository {

	// 営業リスト検索
	List<SalesEntity> getSalesSearch(SalesEntity salseHistory);

	// 営業情報登録
	int insertSalse(SalesEntity salseHistory);

	// 営業会社ステータス
	int insertSalseStats(SalelistStatusHi salseStats);

	// IDの最大値を取得する
	String getMaxId();

	// 営業ステータスを登録する
	int insertSaleStats(SalelistStatusHi salesStats);

	// 営業リストの更新を行う
	int updateSalseById(@Param("list") List<SalesEntity> list);

}
