package com.example.demo.service.CS4;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesAchievementsDto;

@Service
public class SalesAchievementsService {

	public List<SalesAchievementsDto> salesAchievement(SalesAchievementsDto sale) {
		
		List<SalesAchievementsDto> result = test(sale);

		return result;

	}

	public List<SalesAchievementsDto> test(SalesAchievementsDto sales) {

		List<SalesAchievementsDto> salesAchievementsList = new ArrayList<SalesAchievementsDto>();

		SalesAchievementsDto test = new SalesAchievementsDto();

		test.setUserId(sales.getUserId());

		test.setUserCompanyCode(sales.getUserCompanyCode());

		test.setUserId("user001");
		test.setUserCompanyCode("COMP01");
		test.setUserTeamCode("TEAM01");

		for (int i = 0; i <= 1000; i++) {
			
			test.setCallCount(120 + i++);
			
			test.setConnectCount(45 + i++);
			
			test.setOwnerCount(18 + i++);
			
			test.setFullCount(10 + i++);
			
			test.setAppointmentCount(4 + i++);
			
			test.setKpiAchievementRate(80.0 + i++);

			salesAchievementsList.add(test);
		}

		return salesAchievementsList;

	}

}
