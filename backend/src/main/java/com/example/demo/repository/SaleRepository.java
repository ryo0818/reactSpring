package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.SaleHistoryEntity;
import com.example.demo.entity.SalesEntity;

@Mapper
public interface SaleRepository {

	// 営業リスト検索
	List<SalesEntity> getSalesSearch(SalesEntity salseHistory);

	// 営業情報登録
	int insertSale(SalesEntity salseHistory);

	// 営業会社ステータス
	int insertSalseStats(SaleHistoryEntity salseStats);

	// IDの最大値を取得する
	String getMaxSalesId();

	// 営業ステータスを登録する
	int insertSaleStats(SaleHistoryEntity salesStats);

	// 営業リストの更新を行う
	int updateSaleBySaleId(@Param("list") List<SalesEntity> list);

}
