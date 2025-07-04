package com.example.demo.constats;

import java.util.Map;

public class CommonConstants {

	// ローカル環境
	public static final String PROFILE_LOCAL = "local";

	// 開発環境
	public static final String PROFILE_DEV = "dev";

	// 本番環境
	public static final String PROFILE_PROD = "prod";

	// デフォルト（ローカル環境）
	public static final String PROFILE_DEFAULT = "default";

	// 環境変換
	public static final Map<String, String> PROFILE_NAME_MAP = Map.of(
		PROFILE_DEFAULT, "デフォルト環境",
		PROFILE_LOCAL, "ローカル環境",
		PROFILE_DEV, "開発環境",
		PROFILE_PROD, "本番環境");

	// アスタリスク
	public static final String ASTERISK = "*";

	// 文字コード
	public static final String UTF8 = "UTF-8";

	// messages.propertiesファイル名
	public static final String PROPERTIES_FILE_NAME_MESSAGE = "messages";
}
