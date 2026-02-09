package com.example.demo.dto;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/*
 * 営業実績集計クラス
 * 
 */
@Getter
@Setter
public class SalesAchievementsDto {
	
	/** ユーザーID（担当者ID） */
	private String userId;

	/** 所属会社コード */
	private String userCompanyCode;

	/** 所属チームコード */
	private String userTeamCode;
	
	/** 時間単位 */
    private String timeUnit;

    /** 架電数 */
    private int callCount;

    /** 接続数 */
    private int connectCount;

    /** オーナ数 */
    private int ownerCount;

    /** フル */
    private int fullCount;

    /** アポ数 */
    private int appointmentCount;

    /** KPI達成率 */
    private double kpiAchievementRate;
    
	/** 検索開始日 */
    private LocalDateTime searchStartDate;
    
	/** 検索終了日 */
    private LocalDateTime searchEndDate;
    
}
