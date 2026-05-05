package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
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

		// [確認] セッションチェック開始
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2003, CommonConstants.LOG_LV_DEBUG, null, (String[]) null);

		// サーバーのリクエスト情報を取得
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

		// 既存セッションのみを取得する（falseを渡すことで新規セッションは作らない）
		HttpSession httpSession = request.getSession(false);

		// セッション自体がnullの場合（未ログイン or タイムアウト）
		if (httpSession == null) {
			applicationLogger.outDebugLog("[SESSION] セッションがnullです。未ログインまたはタイムアウト。");
			throw new UnauthorizedException();
		}

		// [確認] セッション存在確認OK
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2004, CommonConstants.LOG_LV_DEBUG, null, httpSession.getId());

		// セッションからユーザー情報を取得する
		UserSessionEntity userSession = (UserSessionEntity) httpSession.getAttribute(UserSessionInfo.ATTR_USER);

		// セッション属性(userInfo)がnullの場合（ログイン処理が未完了）
		if (userSession == null) {
			applicationLogger.outDebugLog("[SESSION] セッション属性(userInfo)がnullです。ログイン処理が未完了の可能性があります。");
			throw new UnauthorizedException();
		}

		// [確認] セッション属性(userInfo)取得OK
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2005, CommonConstants.LOG_LV_DEBUG, null, userSession.getSessionUserId());

		// セッション情報確認（セッションユーザーIDが空の場合）
		if (!StringUtils.hasText(userSession.getSessionUserId())) {
			applicationLogger.outDebugLog("[SESSION] セッションユーザーIDが空です。");
			throw new ForbiddenException("セッションユーザーID");
		}

		// [確認] セッションユーザーID確認OK
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2006, CommonConstants.LOG_LV_DEBUG, null, userSession.getSessionUserId());

		// セッション情報確認（会社コードが空の場合）
		if (!StringUtils.hasText(userSession.getMycompanycode())) {
			applicationLogger.outDebugLog("[SESSION] 会社コードが空です。");
			throw new ForbiddenException("会社コード");
		}

		// [確認] 会社コード確認OK
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2007, CommonConstants.LOG_LV_DEBUG, null, userSession.getMycompanycode());

		// セッションユーザーIDをMDCに設定（以降のログに自動付与される）
		org.slf4j.MDC.put("usid", userSession.getSessionUserId());

		// [確認] セッションチェック完了
		applicationLogger.outLogMessage(MessagesPropertiesConstants.LOG_2008, CommonConstants.LOG_LV_DEBUG, null, userSession.getSessionUserId());
	}
}
