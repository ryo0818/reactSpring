package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.dto.StatusCountDto;
import com.example.demo.entity.SaleHistoryEntity;

@Mapper
public interface SaleHistoryRepository {


	// 営業ステータスを登録する
	int insertSaleStats(SaleHistoryEntity salesStats);
	
	List<StatusCountDto> selectStatusCountByTeamId(String string);
}
