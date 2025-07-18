package com.example.demo.model;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/**
 * 架電アクション履歴エンティティ
 * テーブル：call_action_history に対応
 */
@Getter
@Setter
public class CallActionHistoryEntity {

	/** 組織コード */
	private String orgCode;

	/** ユーザーID */
	private String userId;

	/** 架電先の会社名 */
	private String companyName;

	/** 架電先の電話番号 */
	private String phoneNumber;

	/** 架電日付 */
	private LocalDate callDate;

	/** 架電数 */
	private Integer callCount;

	/** 状況 */
	private String status;

	/** 架電対応した担当者名 */
	private String staffName;

	/** 備考 */
	private String note;
}
