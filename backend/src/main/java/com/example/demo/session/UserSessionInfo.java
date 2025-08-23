package com.example.demo.session;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.example.demo.entity.RegUserEntity;

/*
 * セッション管理クラス
 * 
 */
@Component
public class UserSessionInfo {

	// セッション属性キー
	public static final String ATTR_USER = "userInfo";

	/*
	 * ユーザー情報設定
	 */
	public void setUserInfo(RegUserEntity regUser, HttpSession session, HttpServletRequest request) {

		// セッションID発行
		request.changeSessionId();

		// セッションユーザー情報
		UserSessionEntity userSession = new UserSessionEntity();

		// ユーザー名
		userSession.setUsername(regUser.getUsername());

		// 会社コード
		userSession.setMycompanycode(regUser.getMycompanycode());

		// メールアドレス
		userSession.setEmail(regUser.getEmail());

		// 権限
		userSession.setAdminLevel(regUser.getAdminLevel());

		// ユーザー情報を設定
		session.setAttribute(ATTR_USER, userSession);
	}
}
