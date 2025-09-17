package com.example.demo.entity;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoEntity {

	/** ユーザーID（社員番号などを格納） */
	private String userId;

	/** ユーザー名（氏名） */
	private String userName;

	/** 所属会社コード */
	private String userCompanyCode;

	/** 所属チームコード */
	private String userTeamCode;

	/** メールアドレス */
	private String userEmail;

	/** 管理者レベル */
	private Integer adminLevel;

	/** 有効開始日 */
	private LocalDate validStartDate;

	/** 有効終了日 */
	private LocalDate validEndDate;

	/** 有効フラグ（true=有効, false=無効） */
	private Boolean validFlg;
}
