package com.example.demo.session;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

/*
 * ログインユーザーの情報設定
 */
@Getter
@Setter
public class AuthenticatedUser implements Serializable {

	/** ユーザーID */
	private String loginUserId;

	/** 権限 */
	private String loginUserRole;

	/** ユーザーメールアドレス */
	private String loginUserMailAddress;

	/** ログイン日時 */
	private LocalDateTime loginTime;

	/** ユーザーIPアドレス */
	private String ipAddress;

	/** セッションID */
	private String sessionId;
}
