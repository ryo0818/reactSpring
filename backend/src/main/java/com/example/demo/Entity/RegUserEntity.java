package com.example.demo.Entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegUserEntity {

	/** ユーザID */
	private String id;

	/** メールアドレス */
	private String email;

	/** 所属会社コード */
	private String myCompanyCode;

	/** 有効開始日付 */
	private LocalDate validStartDate;

	/** 有効終了日付 */
	private LocalDate validEndDate;

	/** 管理者レベル */
	private Integer adminLevel;

	/** 有効フラグ */
	private Boolean validFlg;
}
