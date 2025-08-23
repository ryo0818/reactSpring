package com.example.demo.session;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSessionEntity {

	/** セッションユーザーキー*/
	private String sessionUserId;

	/** ユーザ名 */
	private String username;

	/** メールアドレス */
	private String email;

	/** ユーザー会社コード */
	private String mycompanycode;

	/** 管理者レベル */
	private Integer adminLevel;

	/**
	 * セッション情報を空にする
	 */
	public void clear() {
		this.sessionUserId = null;
		this.username = null;
		this.email = null;
		this.mycompanycode = null;
		this.adminLevel = null;
	}
}
