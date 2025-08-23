package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

/*
 * ユーザー情報
 * 
 */
@Getter
@Setter
public class RegUserEntity {

	/** ユーザID */
	private String id;

	/** ユーザ名 */
	private String username;

	/** メールアドレス */
	private String email;

	/** ユーザー会社コード */
	private String mycompanycode;

	/** 有効開始日付 */
	private LocalDate validStartDate;

	/** 有効終了日付 */
	private LocalDate validEndDate;

	/** 管理者レベル */
	private Integer adminLevel;

	/** 有効フラグ */
	private Boolean validFlg;

	/** ユーザー情報取得結果 */
	private boolean resultStatus;

}
