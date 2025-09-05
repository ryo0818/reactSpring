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
import com.example.demo.entity.RegUserEntity;

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
	public void setUserInfo(RegUserEntity regUser, HttpSession session, HttpServletRequest request) {

		// セッションID発行
		request.changeSessionId();

		// セッションユーザー情報
		UserSessionEntity userSession = new UserSessionEntity();

		// セッションユーザーIDを発行する
		String sessionUserId = CommonConstants.SUID + alphaNum10(CommonConstants.SUID_NUM);

		// セッションユーザーID
		userSession.setSessionUserId(sessionUserId);

		// ユーザー名
		userSession.setUsername(regUser.getUsername());

		// 会社コード
		userSession.setMycompanycode(regUser.getMycompanycode());

		// メールアドレス
		userSession.setEmail(regUser.getEmail());

		// 権限
		userSession.setAdminLevel(regUser.getAdminLevel());

		// セッション情報にユーザー情報を設定する。
		session.setAttribute(ATTR_USER, userSession);

		// ログにセッションユーザーIDを設定
		MDC.put("usid", sessionUserId);

		// ログ出力
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
