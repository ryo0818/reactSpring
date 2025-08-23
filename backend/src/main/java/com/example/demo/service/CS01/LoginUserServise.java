package com.example.demo.service.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.entity.RegUserEntity;
import com.example.demo.repository.UserLoginRepository;

@Service
public class LoginUserServise {

	@Autowired
	UserLoginRepository userLoginRepository;

	@Autowired
	ApplicationLogger logger;

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
	public String checkCompanyCode(String mycompanycode) {

		// 取得件数を格納
		int result = userLoginRepository.checkCompanyCode(mycompanycode);

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
	public int insertUser(RegUserEntity regUser) throws Exception {

		// 登録結果
		int result = 0;

		try {
			// ユーザー情報登録件数
			result = userLoginRepository.insertUser(regUser);

			// 重複キーの場合は正常終了
		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_2002, CommonConstants.LOG_LV_DEBUG, null, (String[]) null);
			return result;
		}

		return result;
	}
}
