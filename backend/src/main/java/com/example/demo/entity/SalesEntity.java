package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesEntity {

	/** ユーザID */
	private String id;

	/** 所属会社コード */
	private String mycompanycode;

	/** チームコード */
	private String myteamcode;

	/** 業界 */
	private String industry;

	/** 会社名 */
	private String companyName;

	/** 電話番号 */
	private String phoneNumber;

	/** 架電日 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime callDate;

	/** 架電数 */
	private Integer callCount;

	/** ステータス */
	private String status;

	/** 担当者 */
	private String staff;

	/** 会社URL */
	private String url;

	/** 住所 */
	private String address;

	/** 備考 */
	private String remarks;

	/** 検索 */
	private String Search;

	/** 優先度高フラグ */
	private Boolean hotFlg;

	/** ユーザーID */
	private String userId;

	/** 登録日付 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime insertdatetime;

	private String validFlg;

}
