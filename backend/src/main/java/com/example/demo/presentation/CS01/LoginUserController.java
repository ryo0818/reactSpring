package com.example.demo.presentation.CS01;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constats.CommonConstants;
import com.example.demo.entity.RegUserEntity;
import com.example.demo.service.CS01.LoginUserServise;
import com.example.demo.session.UserSessionInfo;

/*
 * ログイン処理・新規登録処理
 * 
 */
@RestController
@RequestMapping("/login")
public class LoginUserController {

	@Autowired
	LoginUserServise loginUserServise;

	@Autowired
	UserSessionInfo useSession;

	/*
	 * ユーザーIDとメールアドレスから該当するユーザー情報を返却する。
	 * @param id ユーザーID
	 * @param email  メールアドレス
	 * 
	 * @return RegUserEntity ユーザー情報
	 */
	@PostMapping("/get-user-info")
	public RegUserEntity getUserInfo(
		@RequestBody RegUserEntity regUser,
		HttpServletRequest request,
		HttpSession session) throws Exception {

		// ユーザーID
		String userId = regUser.getId();

		// メールアドレス
		String email = regUser.getEmail();

		// ユーザーIDまたはメールアドレスが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(userId) || !StringUtils.hasText(email)) {

			// ユーザー情報
			RegUserEntity result = new RegUserEntity();

			// ユーザー情報取得結果にFALSEを設定する
			result.setResultStatus(false);
			return result;
		}

		// ユーザー情報確認
		RegUserEntity result = loginUserServise.getUserInfo(userId, email);

		// 情報が取得できなかった場合は終了
		if (!result.isResultStatus()) {
			return result;
		}
		// セッションにユーザー情報を設定する(セッション処理廃止のため)
		// useSession.setUserInfo(result, session, request);
		return result;
	}

	/*
	 * 会社コードをの存在チェックを行う
	 * @param mycompanycode 会社コード
	 * 
	 * @return RegUserEntity ユーザー情報
	 */
	@PostMapping("/check-cmpcode")
	public String cheackCompanyCode(@RequestBody RegUserEntity regUser) throws Exception {

		// ユーザー会社コード
		String mycompanycode = regUser.getMycompanycode();

		// ユーザー名
		String userName = regUser.getUsername();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// ユーザー名が存在しない場合は処理を終了する。
		if (!StringUtils.hasText(userName)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 取得結果
		String result = loginUserServise.checkCompanyCode(mycompanycode);

		return result;
	}

	/*
	 * ユーザー情報の新規登録を行う
	 * @param id ユーザーID
	 * @param username ユーザー名
	 * @param email メールアドレス
	 * @param mycompanycode 会社コード
	 * @param validStartDate 有効開始日付
	 * @param validEndDate 有効終了日付
	 * @param adminLevel 管理者レベル
	 * 
	 * @return 登録結果数
	 *
	 */
	@PostMapping("/insert-user")
	public String insertUser(@RequestBody RegUserEntity regUser) throws Exception {

		// IDが存在しない場合は処理を終了する。
		if (regUser.getId().isEmpty()) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// ユーザー情報を登録する
		String result = String.valueOf(loginUserServise.insertUser(regUser));

		return result;

	}
}
