package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesEntity {

	/** 営業ID */
	private String saleId;

	/** ユーザーID（担当者ID） */
	private String userId;

	/** 所属会社コード */
	private String userCompanyCode;

	/** 所属チームコード */
	private String userTeamCode;

	/** 顧客業種 */
	private String clientIndustry;

	/** 顧客会社名 */
	private String clientCompanyName;

	/** 顧客電話番号 */
	private String clientPhoneNumber;

	/** 架電日時 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime callDateTime;

	/** 架電回数 */
	private Integer callCount;

	/** ステータス名 */
	private String statusName;

	/** 担当者名 */
	private String userStaff;

	/** 備考 */
	private String remarks;

	/** 顧客URL */
	private String clientUrl;

	/** 顧客住所 */
	private String clientAddress;

	/** ホットフラグ */
	private Boolean hotflg;

	/** 登録日時 */
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime insertDateTime;

	/** 有効フラグ */
	private Boolean validFlg;

}
