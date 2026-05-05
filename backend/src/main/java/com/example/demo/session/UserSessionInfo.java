package com.example.demo.session;

import java.security.SecureRandom;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.entity.UserInfoEntity;

/*
 * セッション管理クラス
 * 
 */
@Component
public class UserSessionInfo {

	// セッション属性キー
	public static final String ATTR_USER = "userInfo";

	private static final String ALPHANUM = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	private static final SecureRandom RNG = new SecureRandom();

	@Autowired
	ApplicationLogger logger;

	/*
	 * ユーザー情報設定
	 */
	public void setUserInfo(UserInfoEntity regUser, HttpSession session, HttpServletRequest request) {

		// [確認] セッション設定開始ログ
		logger.outLogMessage(MessagesPropertiesConstants.LOG_2001, CommonConstants.LOG_LV_DEBUG, null, regUser.getUserId());

		// セッションID発行（固定攻撃対策：古いセッションIDを無効化して新しいIDを発行）
		request.changeSessionId();

		// セッションユーザー情報
		UserSessionEntity userSession = new UserSessionEntity();

		// セッションユーザーIDを発行する
		String sessionUserId = CommonConstants.SUID + alphaNum10(CommonConstants.SUID_NUM);

		// セッションユーザーID
		userSession.setSessionUserId(sessionUserId);

		// ユーザー名
		userSession.setUsername(regUser.getUserName());

		// 会社コード
		userSession.setMycompanycode(regUser.getUserCompanyCode());

		// メールアドレス
		userSession.setEmail(regUser.getUserEmail());

		// 権限
		userSession.setAdminLevel(regUser.getAdminLevel());

		// セッション情報にユーザー情報を設定する。
		session.setAttribute(ATTR_USER, userSession);

		// ログにセッションユーザーIDを設定
		MDC.put("usid", sessionUserId);

		// [確認] セッション設定完了ログ（保存した全項目を出力）
		logger.outLogMessage(MessagesPropertiesConstants.LOG_2002, CommonConstants.LOG_LV_DEBUG, null,
				session.getId(),
				sessionUserId,
				regUser.getUserName(),
				regUser.getUserCompanyCode(),
				String.valueOf(regUser.getAdminLevel()));

		// セッション設定完了ログ（INFO）
		logger.outLogMessage(MessagesPropertiesConstants.LOG_1002, CommonConstants.LOG_LV_INFO, null, sessionUserId);
	}

	/*
	 * セッション情報から会社コードを取得する
	 */
	public static final String getMycompanycode(HttpSession session) {

		// セッションからユーザー情報を取得
		UserSessionEntity userSession = (UserSessionEntity) session.getAttribute(UserSessionInfo.ATTR_USER);

		// ユーザー会社コード
		String mycompanycode = userSession.getMycompanycode();

		return mycompanycode;
	}

	/*
	 * 英数字ランダム文字列10文字
	 */
	public static String alphaNum10(int num) {
		char[] buf = new char[num];
		for (int i = 0; i < buf.length; i++) {
			buf[i] = ALPHANUM.charAt(RNG.nextInt(ALPHANUM.length()));
		}
		return new String(buf);
	}
}
