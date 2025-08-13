package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaleListEntity {
	/** ユーザID */
	private String id;

	/** 所属会社コード */
	private String myCompanyCode;

	/** 会社名 */
	private String companyName;

	/** 電話番号 */
	private String phoneNumber;

	/** 架電日 */
	private LocalDate callDate;

	/** 架電数 */
	private Integer callCount;

	/** ステータス */
	private String status;

	/** 担当者 */
	private String staff;

	/** 備考 */
	private String remarks;

	/** 会社URL */
	private String url;

	/** 住所 */
	private String address;

	/** 優先度高フラグ */
	private Boolean hotFlg;
}
