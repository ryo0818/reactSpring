package com.example.demo.config;

import java.util.Locale;

import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;

import com.example.demo.constats.CommonConstants;

/*
 * メッセージプロパティ取得クラス
 */

@Component
public class MessagesPropertiesConfig {

	// 取得できなかった場合の文字
	private String ERROR_GET_PROPERTY = "メッセージプロパティの取得に失敗しました。";

	/*
	 * メッセージプロパティからログメッセージを表示する
	 * 
	 */
	public String getMessagesProperties(String msCode, String... args) {

		// プロパティファイルソース
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();

		// プロパティファイル名を設定
		messageSource.setBasename(CommonConstants.PROPERTIES_FILE_NAME_MESSAGE);

		// 文字コードを設定
		messageSource.setDefaultEncoding(CommonConstants.UTF8);

		// プロパティファイルからメッセージを取得
		String message = messageSource.getMessage(msCode, args, ERROR_GET_PROPERTY, Locale.getDefault());

		return message;
	}
}
