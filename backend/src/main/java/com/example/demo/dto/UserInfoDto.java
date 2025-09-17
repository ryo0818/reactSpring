package com.example.demo.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/*
 * ユーザー情報
 */
@Getter
@Setter
public class UserInfoDto {

	/* ユーザーID */
	private String userId;
	
	/* ユーザー名 */
	private String userName;
	
	/* 所属会社コード */
	private String userCompanyCode;
	
	/* チームコード */
	private String userTeamCode;
	
	/* メールアドレス */
	private String userEmail;
	
	/** 管理者レベル */
	private Integer adminLevel;

	/** 有効開始日付 */
	private LocalDate validStartDate;

	/** 有効終了日付 */
	private LocalDate validEndDate;
	
	/** ユーザー情報取得結果 */
	private boolean resultStatus;
	
}
