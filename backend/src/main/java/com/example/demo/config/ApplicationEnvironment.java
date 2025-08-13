package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.example.demo.constats.CommonConstants;

/**
 * 環境設定クラス
 */
@Configuration
public class ApplicationEnvironment {

	private final Environment environment;

	public ApplicationEnvironment(Environment environment) {
		this.environment = environment;
	}

	// ローカル環境用環境
	@Value("${app.cors.origin.local:}")
	private String LOCAL_CORS;

	// 開発環境用環境
	@Value("${app.cors.origin.dev:}")
	private String DEV_CORS;

	// 本番環境用環境
	@Value("${app.cors.origin.prod:}")
	private String PROD_CORS;

	// APIへのURLパターン
	private static final String URL_API = "/**";

	/**
	 * プロファイルごとにCORSの許可環境を切り替える
	 * 
	 * @return WebMvcConfigurer
	 */
	@Bean
	WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				// 現在有効なプロファイルを取得
				String[] activeProfiles = environment.getActiveProfiles();

				// 環境を設定
				String[] allowedOrigins = null;

				// 開発環境の場合
				if (containsProfile(activeProfiles, CommonConstants.PROFILE_DEV)) {

					// ローカル・開発環境を設定
					allowedOrigins = new String[] {
						LOCAL_CORS, DEV_CORS };

				} else
				// 本番環境の場合
				if (containsProfile(activeProfiles, CommonConstants.PROFILE_PROD)) {

					// 本番環境を設定
					allowedOrigins = new String[] {
						PROD_CORS };
				} else
				// その他
				if (containsProfile(activeProfiles, CommonConstants.PROFILE_LOCAL)) {
					// ローカル環境のみを設定
					allowedOrigins = new String[] {
						LOCAL_CORS };
				} else {
					System.out.println("読み込まれてない");
				}
				// CORSマッピングを登録：
				registry.addMapping(URL_API)
					// 設定された環境を適用
					.allowedOrigins(allowedOrigins)
					// HTTPメソッドを指定
					.allowedMethods(CommonConstants.ASTERISK);
			}
		};
	}

	/**
	 * 現在有効なプロファイルに指定された名前が含まれているかを判定
	 *
	 * @param profiles 現在のアクティブプロファイル
	 * @param target   チェック対象のプロファイル名
	 * @return 含まれていれば true
	 */
	private boolean containsProfile(String[] profiles, String target) {
		for (String profile : profiles) {
			if (profile.equalsIgnoreCase(target)) {
				return true;
			}
		}
		return false;
	}
}
