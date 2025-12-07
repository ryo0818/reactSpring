package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleHistoryEntity {

	/* レコードID */
	private String saleId;

	/* チームコード */
	private String userTeamCode;

	/* ステータス */
	private String statusName;

	/* 登録日付 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime insertDateTime;

	/** 有効フラグ（true=有効, false=無効） */
	private Boolean validFlg;

		/** ステータスID */
	private Integer statusId;
	
	/** メディア */
	private String media;
	
}
