package com.example.demo.Entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MyCompanyentity {

	/** ユーザID */
	private String id;

	/** メールアドレス */
	private String email;

	/** 所属会社コード */
	private String mycompanycode;

	/** 有効開始日付 */
	private LocalDate validstartdate;

	/** 有効終了日付 */
	private LocalDate validenddate;

	/** 管理者レベル */
	private Integer adminlevel;

	/** 有効フラグ */
	private Boolean validflg;

}
