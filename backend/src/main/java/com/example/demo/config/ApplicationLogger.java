package com.example.demo.config;

import org.springframework.stereotype.Component;

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

}
