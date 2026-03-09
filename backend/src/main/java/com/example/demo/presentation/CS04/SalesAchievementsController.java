package com.example.demo.presentation.CS04;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.service.CS02.SalesService;
import com.example.demo.service.CS04.SalesAchievementsService;

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

		List<SalesAchievementsDto> result = new ArrayList<SalesAchievementsDto>();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(achievement.getUserCompanyCode())) {
			return result;
		}

		// チームコードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(achievement.getUserTeamCode())) {
			return result;
		}

		// 時間単位が存在しない場合は処理を終了する。
		if (!StringUtils.hasText(achievement.getTimeUnit())) {
			return result;
		}

		// 時間単位を設定する
		createDateRange(achievement);
		
		// 営業実績集計から検索を行う
		result = salesAchievementsService.salesAchievement(achievement);
		
		

		return result;

	}

	/*
	 * 時間設定を行うクラス
	 */
	public void createDateRange(SalesAchievementsDto salesAchievements) {

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime searchStartDate;
		LocalDateTime searchEndDate;

		switch (salesAchievements.getTimeUnit()) {

		// 時間別（今日全体：時間ごとに集計）
		case HOURLY_UNIT:
			searchStartDate = LocalDate.now().atStartOfDay();
			searchEndDate = LocalDate.now().atTime(LocalTime.MAX);
			break;

		// 日別（今月全体：日ごとに集計）
		case DAILY_UNIT:
			searchStartDate = LocalDate.now()
					.with(TemporalAdjusters.firstDayOfMonth())
					.atStartOfDay();
			searchEndDate = LocalDate.now()
					.with(TemporalAdjusters.lastDayOfMonth())
					.atTime(LocalTime.MAX);
			break;

		// 週別（今年全体：週ごとに集計）
		case WEEKLY_UNIT:
			searchStartDate = LocalDate.now()
					.with(TemporalAdjusters.firstDayOfYear())
					.atStartOfDay();
			searchEndDate = LocalDate.now()
					.with(TemporalAdjusters.lastDayOfYear())
					.atTime(LocalTime.MAX);
			break;

		// 月別（今年全体：月ごとに集計）
		case MONTHLY_UNIT:
			searchStartDate = LocalDate.now()
					.with(TemporalAdjusters.firstDayOfYear())
					.atStartOfDay();
			searchEndDate = LocalDate.now()
					.with(TemporalAdjusters.lastDayOfYear())
					.atTime(LocalTime.MAX);
			break;

		default:
			throw new IllegalArgumentException("不正な検索単位: ");
		}

		salesAchievements.setSearchStartDate(searchStartDate);
		salesAchievements.setSearchEndDate(searchEndDate);
	}

}
