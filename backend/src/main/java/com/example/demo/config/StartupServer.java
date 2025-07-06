package com.example.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;

/*
 * SpringBoot起動時の処理
 */
@Component
public class StartupServer {

	@Autowired
	private Environment environment;

	@Autowired
	private ApplicationLogger applicationLogger;

	@Autowired
	private MessagesPropertiesConfig messagesPropertiesConfig;

	/*
	 * SpringBoot起動時に環境をログ出力する
	 */
	@EventListener(ApplicationReadyEvent.class)
	public void onApplicationReadyServerStartupLogger() {
		// 起動時環境を取得
		String[] activeProfiles = environment.getActiveProfiles();
		String profile = (activeProfiles.length > 0)
			? activeProfiles[0]
			: CommonConstants.PROFILE_DEFAULT;

		// 環境を設定
		String profileName = CommonConstants.PROFILE_NAME_MAP.getOrDefault(profile, profile);

		// スタートログを取得
		String startInfoLog = messagesPropertiesConfig.getMessagesProperties(
			// サーバースタートログ
			MessagesPropertiesConstants.SERVER_START,
			// 環境
			profileName);

		// ログ出力
		applicationLogger.outInfoLog(startInfoLog);
	}
}
