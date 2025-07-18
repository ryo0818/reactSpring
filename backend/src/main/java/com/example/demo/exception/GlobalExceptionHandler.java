package com.example.demo.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.config.MessagesPropertiesConfig;
import com.example.demo.constats.MessagesPropertiesConstants;

/*
 * 例外キャッチクラス
 * 
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private ApplicationLogger logInfo;

	@Autowired
	private MessagesPropertiesConfig messagesPropertiesConfig;

	// 全例外を受け取る
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleException(Exception e) {

		// クラス名取得
		String className = e.getStackTrace()[0].getClassName();

		// メソッド名取得
		String methodName = e.getStackTrace()[0].getMethodName();

		// ログメッセージを取得
		logInfo.outErrorLog(
			messagesPropertiesConfig.getMessagesProperties(
				// ログID
				MessagesPropertiesConstants.LOG_9001,
				// クラス名
				className,
				// メソッド名
				methodName),
			e);

		// クライアントに返すメッセージ（簡易でOK）
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("システムエラーが発生しました。しばらくしてから再度お試しください。");
	}
}
