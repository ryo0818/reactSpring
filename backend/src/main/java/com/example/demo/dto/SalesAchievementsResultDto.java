package com.example.demo.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesAchievementsResultDto {

	/** ユーザーID（担当者ID） */
	private String userId;

	/** 所属会社コード */
	private String userCompanyCode;

	/** 所属チームコード */
	private String userTeamCode;
	
	/** 時間単位 */
    private String timeUnit;
    
	/** 検索開始日 */
    private LocalDate searchStartDate;
    
	/** 検索終了日 */
    private LocalDate searchEndDate;
    
    /** 営業履歴 時間別集計リスト */
    List<SaleHistoryAggDto> saleHistoryAggList;

    /** ファネル集計リスト（時間単位ごと） */
    List<SaleFunnelAggDto> saleFunnelAggList;
}
