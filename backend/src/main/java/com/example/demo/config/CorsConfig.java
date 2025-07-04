package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.constats.CommonConstants;

/**
 * ポート番号・URL設定
 */
@Configuration
public class CorsConfig {

	// ローカル環境
	@Value("${app.cors.origin.local}")
	private String LOCAL_CORS;

	// 開発環境
	@Value("${app.cors.origin.dev}")
	private String DEV_CORS;

	// 本番環境
	@Value("${app.cors.origin.prod}")
	private String PROD_CORS;

	// api配下を指定
	private String URL_API = "/api/**";

	/**
	* マッピング設定追加
	* @param CorsRegistry マッピング用レジストリ
	*/
	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// CORSを許可
				registry.addMapping(URL_API)
					// URL設定
					.allowedOrigins(LOCAL_CORS, DEV_CORS, PROD_CORS)
					// HTTPメソッド設定
					.allowedMethods(CommonConstants.ASTERISK);
			}
		};
	}
}
