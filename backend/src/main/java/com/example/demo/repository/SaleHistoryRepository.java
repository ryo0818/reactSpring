package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.SaleHistoryAggDto;
import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.entity.SaleHistoryEntity;

@Mapper
public interface SaleHistoryRepository {

	// 営業ステータスを登録する
	int insertSaleStats(SaleHistoryEntity salesStats);

	// 時間単位・会社コード・日付範囲で集計する
	List<SaleHistoryAggDto> selectAggregatedByUnit(SalesAchievementsDto dto);
}
