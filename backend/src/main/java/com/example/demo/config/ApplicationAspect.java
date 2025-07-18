package com.example.demo.config;

import jakarta.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.demo.constats.MessagesPropertiesConstants;

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

	/*
	 * コントローラークラス開始時ログを出力させる処理
	 */
	@Before("execution(* com.example.demo.presentation..*.*(..))")
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
}
