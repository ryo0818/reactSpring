package com.example.demo.service.CS4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.dto.StatusCountDto;
import com.example.demo.repository.SaleHistoryRepository;




@Service
public class SalesAchievementsService {
	
	
	@Autowired
	SaleHistoryRepository saleHistoryRepository;

	public List<SalesAchievementsDto> salesAchievement(SalesAchievementsDto sale) {
		
		// 結果
		List<SalesAchievementsDto> result = new ArrayList<>();
		
		List<StatusCountDto> syukei = saleHistoryRepository.selectStatusCountByTeamId(sale.getUserTeamCode());
		
		
		return result;

	}

}
