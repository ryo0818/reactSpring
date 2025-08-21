package com.example.demo.service.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.constats.CommonConstants;
import com.example.demo.entity.RegUserEntity;
import com.example.demo.repository.UserLoginRepository;

@Service
public class LoginUserServise {

	@Autowired
	UserLoginRepository userLoginRepository;

	/*
	 * ユーザー情報確認
	 * 
	 */
	@Transactional(readOnly = true)
	public RegUserEntity getUserInfo(String userId, String email) {

		// 取得結果を設定
		RegUserEntity result = userLoginRepository.getUserInfo(userId, email);

		// ユーザー情報が取得されない場合
		if (result == null) {

			// ユーザー情報
			result = new RegUserEntity();

			// ユーザー情報取得結果にFALSEを設定する
			result.setResultStatus(false);

			return result;
		}

		// ユーザー情報取得結果にTRUEを設定する
		result.setResultStatus(true);

		return result;
	}

	/*
	 * 会社コードの存在チェック
	 * 
	 */
	@Transactional(readOnly = true)
	public String checkCompanyCode(String companyCode) {

		// ユーザー所属会社コード入力確認
		if (!companyCode.contains("-")) {
			companyCode = companyCode.substring(0, 3) + "-" + companyCode.substring(3);
		}

		// 取得件数を格納
		int result = userLoginRepository.checkCompanyCode(companyCode);

		// 取得結果が0件の場合は処理を終了する
		if (result == 0) {
			return CommonConstants.FLG_ZERO;
		}

		// 取得結果「1」を設定
		return CommonConstants.FLG_ONE;
	}

	/*
	 * 新規登録
	 * 
	 */
	@Transactional
	public int insertUser(RegUserEntity regUser) {

		// 
		System.out.println(
			"id=" + regUser.getId() +
				", username=" + regUser.getUsername() +
				", email=" + regUser.getEmail() +
				", companyCode=" + regUser.getCompanyCode() +
				", validStartDate=" + regUser.getValidStartDate() +
				", validEndDate=" + regUser.getValidEndDate() +
				", adminLevel=" + regUser.getAdminLevel() +
				", validFlg=" + regUser.getValidFlg() +
				", resultStatus=" + regUser.isResultStatus());
		// ユーザー情報登録件数
		int result = userLoginRepository.insertUser(regUser);
		return result;
	}
}
