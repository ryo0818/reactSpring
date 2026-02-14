package com.example.demo.presentation.CS04;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.service.CS02.SalesService;
import com.example.demo.service.CS4.SalesAchievementsService;

@RestController
@RequestMapping("/achievment")
public class SalesAchievementsController {

	/** 営業情報サービスクラス */
	@Autowired
	SalesService salesService;
	
	@Autowired
	SalesAchievementsService salesAchievementsService;

	// 単位(時間)
	private static final String HOURLY_UNIT = "1";

	// 単位(日)
	private static final String DAILY_UNIT = "2";

	// 単位(週)
	private static final String WEEKLY_UNIT = "3";

	// 単位(月)
	private static final String MONTHLY_UNIT = "4";

	/*
	 * 検索対象の営業リストを表示する
	 * 
	 * @return 営業履歴リストを表示
	 */
	@PostMapping("/search-sales-achievment")
	public List<SalesAchievementsDto> searchSalesAchievment(
			@RequestBody(required = false) SalesAchievementsDto achievement) throws Exception {

		// 検索単位がからの場合は初期設定を行う。
//		if (achievement.getTimeUnit().isEmpty()) {
//			achievement.setTimeUnit(DAILY_UNIT);
//		}
		
		List<SalesAchievementsDto> salesAchievementsList = new ArrayList<SalesAchievementsDto>();
		
		salesAchievementsList = salesAchievementsService.salesAchievement(achievement);
		

		return salesAchievementsList;

	}
	
	/*
	 * 時間設定を行うクラス
	 */
	 public void createDateRange(SalesAchievementsDto salesAchievements) {

	        LocalDateTime now = LocalDateTime.now();
	        LocalDateTime searchStartDate;
	        LocalDateTime searchEndDate;

	        switch (salesAchievements.getTimeUnit()) {

	            // 時間別（現在の1時間）
	            case HOURLY_UNIT:
	            	searchStartDate = now.withMinute(0).withSecond(0).withNano(0);
	            	searchEndDate = searchStartDate.plusHours(1).minusNanos(1);
	                break;

	            // 日別（今日）
	            case DAILY_UNIT:
	            	searchStartDate = LocalDate.now().atStartOfDay();
	            	searchEndDate = LocalDate.now().atTime(LocalTime.MAX);
	                break;

	            // 週別（今週：月〜日）
	            case WEEKLY_UNIT:
	            	searchStartDate = LocalDate.now()
	                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
	                        .atStartOfDay();
	            	searchEndDate = LocalDate.now()
	                        .with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
	                        .atTime(LocalTime.MAX);
	                break;

	            // 月別（今月）
	            case MONTHLY_UNIT:
	            	searchStartDate = LocalDate.now()
	                        .with(TemporalAdjusters.firstDayOfMonth())
	                        .atStartOfDay();
	            	searchEndDate = LocalDate.now()
	                        .with(TemporalAdjusters.lastDayOfMonth())
	                        .atTime(LocalTime.MAX);
	                break;

	            default:
	                throw new IllegalArgumentException("不正な検索単位: ");
	        }
	        
	        salesAchievements.setSearchStartDate(searchStartDate);
	        salesAchievements.setSearchEndDate(searchEndDate);
	    }

}
