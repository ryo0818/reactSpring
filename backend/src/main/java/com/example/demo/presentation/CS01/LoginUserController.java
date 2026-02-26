package com.example.demo.presentation.CS01;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constats.CommonConstants;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.StatusEntity;
import com.example.demo.service.CS01.LoginUserServise;

/*
 * ログイン処理・新規登録処理
 * 
 */
@RestController
@RequestMapping("/login")
public class LoginUserController {

	@Autowired
	LoginUserServise loginUserServise;

	/*
	 * ユーザーIDとメールアドレスから該当するユーザー情報を返却する。
	 * @param id ユーザーID
	 * @param email  メールアドレス
	 * 
	 * @return RegUserEntity ユーザー情報
	 */
	@PostMapping("/get-user-info")
	public UserInfoDto getUserInfo(
			@RequestBody UserInfoDto userInfo,
			HttpServletRequest request,
			HttpSession session) throws Exception {

		// ユーザーID
		String userId = userInfo.getUserId();

		// メールアドレス
		String email = userInfo.getUserEmail();

		// ユーザー情報取得結果
		UserInfoDto result = new UserInfoDto();

		// ユーザーIDまたはメールアドレスが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(userId) || !StringUtils.hasText(email)) {

			// ユーザー情報取得結果にFALSEを設定する
			result.setResultStatus(false);

			return result;
		}

		// DBから取得したユーザー情報を返却する
		result = loginUserServise.getUserInfo(userId, email);

		return result;
	}

	/*
	 * 会社コードをの存在チェックを行う
	 * @param mycompanycode 会社コード
	 * 
	 * @return RegUserEntity ユーザー情報
	 */
	@PostMapping("/check-cmpcode")
	public String checkCompanyCode(@RequestBody UserInfoDto userInfo) throws Exception {

		// ユーザー会社コード
		String mycompanycode = userInfo.getUserCompanyCode();

		// ユーザー名
		String userName = userInfo.getUserName();

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
	 * ステータス一覧返却
	 * 
	 * @return ステータス
	 */
	@PostMapping("/get-statslist")
	public List<StatusEntity> getStatsList(@RequestBody(required = false) UserInfoDto userInfo) throws Exception {

		// ユーザー会社コード
		String uesrCompanyCode = userInfo.getUserCompanyCode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(uesrCompanyCode)) {
			return List.of();
		}

		// ユーザー所属会社が使用しているステータスを取得する。
		List<StatusEntity> resultStatsList = loginUserServise.getStatsList(uesrCompanyCode);

		return resultStatsList;
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
	public String insertUser(@RequestBody UserInfoDto userInfo) throws Exception {

		// ユーザーIDが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(userInfo.getUserId())) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 会社コードが存在しない場合は処理を終了する
		if (!StringUtils.hasText(userInfo.getUserCompanyCode())) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// チームコードが存在しない場合は処理を終了する
		if (!StringUtils.hasText(userInfo.getUserTeamCode())) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// チームコードチェックを行う
		String teamCodeResult = loginUserServise.checkTeamCode(userInfo.getUserCompanyCode(),
				userInfo.getUserTeamCode());

		// チームコード取得結果が0件の場合は処理を終了する
		if (CommonConstants.FLG_RESULT_FALSE.equals(teamCodeResult)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// ユーザー情報を登録する
		String result = String.valueOf(loginUserServise.insertUser(userInfo));

		return result;

	}
}
