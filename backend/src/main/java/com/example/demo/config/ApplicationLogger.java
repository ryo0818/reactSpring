package com.example.demo.config;

import org.springframework.stereotype.Component;

import com.example.demo.constats.CommonConstants;

import lombok.extern.slf4j.Slf4j;

/*
 * ログメッセージ出力クラス
 */
@Slf4j
@Component
public class ApplicationLogger {

	/*
	 * INFOログ出力
	 */
	public void outInfoLog(String outLog) {
		log.info(outLog);
	}

	/*
	 * DEBUGログ出力
	 */
	public void outDebugLog(String outLog) {
		log.debug(outLog);
	}

	/*
	 * 例外ログ（ERROR）出力
	 */
	public void outErrorLog(String message, Throwable e) {
		log.error(message, e);
	}

	/*
	 * メッセージを設定してログに出力する
	 * @param messageId メッセージID
	 * @param logLevel  ログレベル
	 * @param args[]    メッセージ
	 * 
	 */
	public void outLogMessage(String messageId, String logLevel, Throwable e, String... args) {

		// プロパティファイルソース
		MessagesPropertiesConfig messageSource = new MessagesPropertiesConfig();

		// プロパティファイルからメッセージを取得
		String message = messageSource.getMessagesProperties(messageId, args);

		// ログレベルがINFOの場合はINFOログ出力を行う
		if (CommonConstants.LOG_LV_INFO.equals(logLevel)) {
			outInfoLog(message);
			return;
		}

		// ログレベルがDEBUGの場合はINFOログ出力を行う
		if (CommonConstants.LOG_LV_DEBUG.equals(logLevel)) {
			outDebugLog(message);
			return;
		}

		// エラーログを出力する
		outErrorLog(message, e);
	}
}