package com.example.demo.presentation.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constats.CommonConstants;
import com.example.demo.entity.RegUserEntity;
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
	 * ユーザー情報を取得する。
	 */
	@PostMapping("/get-user-info")
	public RegUserEntity getUserInfo(@RequestBody RegUserEntity regUser) {

		// ユーザーID
		String userId = regUser.getId();

		// メールアドレス
		String email = regUser.getEmail();

		// 会社コードが存在しない場合は処理を終了する。
		if (userId.isEmpty() || email.isEmpty()) {

			// ユーザー情報
			RegUserEntity result = new RegUserEntity();

			// ユーザー情報取得結果にFALSEを設定する
			result.setResultStatus(false);
			return result;
		}

		// ユーザー情報確認
		RegUserEntity result = loginUserServise.getUserInfo(userId, email);

		return result;
	}

	/*
	 * 会社コードをの存在チェックを行う
	 * 
	 */
	@PostMapping("/check-cmpcode")
	public String cheackCompanyCode(@RequestBody RegUserEntity regUser) {

		// ユーザー会社コード
		String companyCode = regUser.getCompanyCode();

		// 会社コードが存在しない場合は処理を終了する。
		if (companyCode.isEmpty()) {
			return CommonConstants.FLG_ZERO;
		}

		// 取得結果
		String result = loginUserServise.checkCompanyCode(companyCode);

		return result;
	}

	/*
	 * ユーザー情報の新規登録を行う
	 */
	@PostMapping("/insert-user")
	public String insertUser(@RequestBody RegUserEntity regUser) {

		// IDが存在しない場合は処理を終了する。
		if (regUser.getId().isEmpty()) {
			return CommonConstants.FLG_ZERO;
		}

		// ユーザー情報を登録する
		String result = String.valueOf(loginUserServise.insertUser(regUser));

		return result;

	}
}
