package com.example.demo.presentation.CS04;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.SaleHistoryAggDto;
import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.dto.SalesAchievementsResultDto;
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
	
	// 単位(時間別)
	private static final String HOUR_UNIT = "1";

	// 単位(日別)
	private static final String DAILY_UNIT = "2";

	// 単位(週別)
	private static final String WEEKLY_UNIT = "3";

	// 単位(月別)
	private static final String MONTHLY_UNIT = "4";

	/*
	 * 検索対象の営業リストを表示する
	 *
	 * @return 営業履歴集計リスト
	 */
	@PostMapping("/search-sales-achievment")
	public SalesAchievementsResultDto searchSalesAchievment(
			@RequestBody(required = false) SalesAchievementsDto achievement) throws Exception {
		
		 // 集計結果
		SalesAchievementsResultDto result = new SalesAchievementsResultDto();
		
		// チームコードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(achievement.getUserTeamCode())) {
			return result;
		}

		// 時間単位が存在しない場合は処理を終了する。
		if (!StringUtils.hasText(achievement.getTimeUnit())) {
			return result;
		}
		
		// ターゲット日付が存在しない場合は現在日付を設定する。
		if (achievement.getTargetDate() == null) {
			achievement.setTargetDate(LocalDateTime.now());
		}

		// 時間単位から集計期間を設定する
		createDateRange(achievement);

		// 営業実績集計から検索を行う
		List<SaleHistoryAggDto> resultList = salesAchievementsService.salesAchievement(achievement);
		
		// 結果をセットする
		result.setSaleHistoryAggList(resultList);

		// ファネル集計をセットする
		result.setSaleFunnelAggList(salesAchievementsService.aggregateFunnel(resultList));

		// ユーザーチームコード、ユーザー会社コード、ユーザーID、時間単位をセットする
		result.setUserTeamCode(achievement.getUserTeamCode());
		result.setUserCompanyCode(achievement.getUserCompanyCode());
		result.setUserId(achievement.getUserId());
		result.setTimeUnit(achievement.getTimeUnit());

		return result;

	}

	/*
	 * 時間設定を行うクラス
	 */
	public void createDateRange(SalesAchievementsDto salesAchievements) {
		
		// ターゲット日付
		LocalDateTime target =  salesAchievements.getTargetDate();

		LocalDateTime searchStartDate;
		LocalDateTime searchEndDate;

		// 時間単位から集計期間を設定する
		switch (salesAchievements.getTimeUnit()) {

			// 時間別（当日：時間ごとに集計）
			case HOUR_UNIT -> {
				searchStartDate = target.toLocalDate().atStartOfDay();
				searchEndDate   = target.toLocalDate().atTime(LocalTime.MAX);
			}

			// 日別（今月全体：日ごとに集計）
			case DAILY_UNIT -> {
				searchStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth()).atStartOfDay();
				searchEndDate   = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth()).atTime(LocalTime.MAX);
			}

			// 週別（今年全体：週ごとに集計）
			case WEEKLY_UNIT -> {
				searchStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
				searchEndDate   = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
			}

			// 月別（今年全体：月ごとに集計）
			case MONTHLY_UNIT -> {
				searchStartDate = LocalDate.now().with(TemporalAdjusters.firstDayOfYear()).atStartOfDay();
				searchEndDate   = LocalDate.now().with(TemporalAdjusters.lastDayOfYear()).atTime(LocalTime.MAX);
			}

			default -> throw new IllegalArgumentException("不正な検索単位: " + salesAchievements.getTimeUnit());
		}

		salesAchievements.setSearchStartDate(searchStartDate);
		salesAchievements.setSearchEndDate(searchEndDate);
	}

}
