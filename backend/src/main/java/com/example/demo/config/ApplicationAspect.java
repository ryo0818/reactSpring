package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.jboss.logging.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.session.UserSessionEntity;
import com.example.demo.session.UserSessionInfo;

/*
 * クラス開始時処理
 */
@Aspect
@Component
public class ApplicationAspect {

	@Autowired
	private ApplicationLogger applicationLogger;

	@Autowired
	MessagesPropertiesConfig messagesPropertiesConfig;

	// 使い回せるポイントカット
	@org.aspectj.lang.annotation.Pointcut("within(com.example.demo.presentation..*)")
	public void inPresentation() {
	}

	@Pointcut("within(com.example.demo.presentation..*) && !within(com.example.demo.presentation.CS01..*)")
	public void inPresentationExceptCS01() {
	}

	@ResponseStatus(HttpStatus.UNAUTHORIZED)
	public class UnauthorizedException extends RuntimeException {
		public UnauthorizedException() {
			// エラーログ出力
			applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_9002, CommonConstants.LOG_LV_ERROR, null, (String[]) null);
		}
	}

	@ResponseStatus(HttpStatus.FORBIDDEN)
	public class ForbiddenException extends RuntimeException {
		public ForbiddenException(String userInfo) {
			// エラーログ出力
			applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_9003, CommonConstants.LOG_LV_ERROR, null, userInfo);
		}
	}

	/*
	 * コントローラークラス開始時ログを出力させる処理
	 */
	@Before("inPresentation()")
	public void logStart(JoinPoint joinPoint) {

		// リクエスト
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		// プロパティファイルからログメッセージを取得
		String startInfoLog = messagesPropertiesConfig.getMessagesProperties(
			// クラス開始ログ
			MessagesPropertiesConstants.LOG_1001,
			// クラス名
			joinPoint.getSignature().getDeclaringTypeName(),
			// メソッド名
			joinPoint.getSignature().getName(),
			// リクエストメソッド
			request.getMethod(),
			// リクエストURL
			request.getRequestURI());

		// ログ出力
		applicationLogger.outInfoLog(startInfoLog);
	}

	/*
	 * セッション情報を確認
	 */
	@Before("inPresentationExceptCS01()")
	public void checkUserSession() {

		// サーバーのリクエスト情報を取得
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		// 既存セッションのみを取得する。
		HttpSession httpSession = request.getSession(false);

		// セッション情報がnullの場合は強制終了する。
		if (httpSession == null) {
			throw new UnauthorizedException();
		}

		// セッションからユーザー情報を取得する。
		UserSessionEntity userSession = (UserSessionEntity) httpSession.getAttribute(UserSessionInfo.ATTR_USER);

		// セッション属性からユーザー情報を取得
		if (userSession == null) {
			// throw new UnauthorizedException();

			// (暫定対応) 会社コードをセットする。
			// セッションユーザー情報
			userSession = new UserSessionEntity();

			// セッションユーザーID
			userSession.setSessionUserId("aaaaaaaaaa");

			// 会社コード
			userSession.setMycompanycode("000-000");

			// セッション情報にユーザー情報を設定する。
			httpSession.setAttribute(UserSessionInfo.ATTR_USER, userSession);
		}

		// セッション情報確認(セッションユーザーID)
		if (!StringUtils.hasText(userSession.getSessionUserId())) {
			throw new ForbiddenException("セッションユーザーID");
		}

		// セッション情報確認(会社コード)
		if (!StringUtils.hasText(userSession.getMycompanycode())) {
			throw new ForbiddenException("会社コード");
		}

		// セッションユーザーIDをログ出力する。
		MDC.put("usid", userSession.getSessionUserId());

	}
}
