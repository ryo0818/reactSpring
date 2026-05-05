package com.example.demo.constats;

public class MessagesPropertiesConstants {

	// サーバースタート時のログメッセージ
	public static final String SERVER_START = "SERVER_START";

	// 処理開始： {0},{1} [{2} {3}]
	public static final String LOG_1001 = "LOG_1001";

	// セッション情報を設定しました。
	public static final String LOG_1002 = "LOG_1002";

	// [SESSION] セッション設定開始。対象ユーザーID:{0}
	public static final String LOG_2001 = "LOG_2001";

	// [SESSION] セッション設定完了。SessionID:{0} / SUID:{1} / ユーザー名:{2} / 会社コード:{3} / adminLevel:{4}
	public static final String LOG_2002 = "LOG_2002";

	// [SESSION] セッションチェック開始。
	public static final String LOG_2003 = "LOG_2003";

	// [SESSION] セッション存在確認 OK。SessionID:{0}
	public static final String LOG_2004 = "LOG_2004";

	// [SESSION] セッション属性(userInfo)確認 OK。SUID:{0}
	public static final String LOG_2005 = "LOG_2005";

	// [SESSION] セッションユーザーID確認 OK。SUID:{0}
	public static final String LOG_2006 = "LOG_2006";

	// [SESSION] 会社コード確認 OK。companyCode:{0}
	public static final String LOG_2007 = "LOG_2007";

	// [SESSION] セッションチェック完了。認証済みユーザー:{0}
	public static final String LOG_2008 = "LOG_2008";

	// エラー9001[処理クラス: {0} メソッド: {1}]
	public static final String LOG_9001 = "LOG_9001";

	// セッションが終了しました。
	public static final String LOG_9002 = "LOG_9002";

	// セッション情報：{0} の取得に失敗しました。
	public static final String LOG_9003 = "LOG_9003";
	
	// 会社コードの取得に失敗しました。
	public static final String LOG_9101 = "LOG_9101";
	
	// チームコードの取得に失敗しました。
	public static final String LOG_9102 = "LOG_9102";
	
	// ユーザー情報の取得に失敗しました。
	public static final String LOG_9103 = "LOG_9103";

	// {0}が重複するため、{1}の登録が失敗しました。
	public static final String LOG_9201 = "LOG_9201";

	// {0}が重複するため、{1}リストの更新が失敗しました。
	public static final String LOG_9202 = "LOG_9202";

	// 入力データ{0}件と更新件数{1}が一致しませんでした。
	public static final String LOG_9203 = "LOG_9203";
}
