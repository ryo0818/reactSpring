package com.example.demo.service.CS04;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SaleHistoryAggDto;
import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.repository.SaleHistoryRepository;

@Service
public class SalesAchievementsService {

	@Autowired
	SaleHistoryRepository saleHistoryRepository;

	/**
	 * 時間別集計結果を返す。
	 */
	public List<SaleHistoryAggDto> salesAchievement(SalesAchievementsDto sale) {
		return saleHistoryRepository.selectAggregatedByUnit(sale);
	}
}
