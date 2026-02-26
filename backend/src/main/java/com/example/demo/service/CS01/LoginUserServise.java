package com.example.demo.service.CS01;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.dto.UserInfoDto;
import com.example.demo.entity.StatusEntity;
import com.example.demo.entity.UserInfoEntity;
import com.example.demo.repository.CompanyInfoRepository;
import com.example.demo.repository.CompanyTeamInfoRepository;
import com.example.demo.repository.SalesStatusRepository;
import com.example.demo.repository.UserInfoRepository;

@Service
public class LoginUserServise {

	@Autowired
	UserInfoRepository userLoginRepository;

	@Autowired
	SalesStatusRepository statsRepository;
	
	@Autowired
	CompanyInfoRepository copnyInfo;
	
	@Autowired
	CompanyTeamInfoRepository companyTeamInfoRepository;
	
	@Autowired
	ApplicationLogger logger;

	/*
	 * ユーザー情報確認
	 * 
	 */
	@Transactional(readOnly = true)
	public UserInfoDto getUserInfo(String userId, String email) {

		// 取得結果を設定
		UserInfoEntity rsUserInfo = userLoginRepository.getUserInfo(userId, email);

		// ユーザー情報
		UserInfoDto result = new UserInfoDto();

		// ユーザー情報が取得されない場合
		if (rsUserInfo == null) {

			// ユーザー情報取得結果にFALSEを設定する
			result.setResultStatus(false);

			// ログメッセージ：ユーザー情報取得失敗
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9103, CommonConstants.LOG_LV_ERROR, null);

			return result;
		}
		// DB取得結果をユーザー情報に設定する
		BeanUtils.copyProperties(rsUserInfo, result);

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
		int result = copnyInfo.checkCompanyCode(mycompanycode);

		// 取得結果が0件の場合は処理を終了する
		if (result == 0) {
			// ログメッセージ：会社コード取得失敗
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9101, CommonConstants.LOG_LV_ERROR, null);
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 取得結果「1」を設定
		return CommonConstants.FLG_RESULT_TRUE;
	}

	/*
	 * チームコードの存在チェック
	 * 
	 */
	@Transactional(readOnly = true)
	public String checkTeamCode(String userCompanyCode, String userTeamCode) {

		// 会社コードとチームコードが存在するか確認を行う
		int result = companyTeamInfoRepository.checkTeamCode(userCompanyCode, userTeamCode);

		// 取得結果が0件の場合は処理を終了する
		if (result == 0) {
			// ログメッセージ：チームコード取得失敗
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9102, CommonConstants.LOG_LV_ERROR, null);

			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 取得結果「1」を設定
		return CommonConstants.FLG_RESULT_TRUE;
	}

	/*
	 * 営業ステータス一覧を取得する
	 */
	public List<StatusEntity> getStatsList(String userCompanyCode) throws Exception {
		return statsRepository.getStatsList(userCompanyCode);
	}

	/*
	 * 新規登録
	 * 
	 */
	@Transactional
	public int insertUser(UserInfoDto userInfo) throws Exception {

		// 登録ユーザー情報
		UserInfoEntity userInfoEntity = new UserInfoEntity();

		// 登録するデータを設定する
		BeanUtils.copyProperties(userInfo, userInfoEntity);

		// 登録結果
		int result = 0;

		try {
			// ユーザー情報登録件数
			result = userLoginRepository.insertUser(userInfoEntity);

			// 重複キーの場合はログメッセージを表示して正常終了を行う。
		} catch (DuplicateKeyException e) {

			// メッセージ内容
			String MS_01 = "ID";
			String MS_02 = "ユーザー情報";

			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9201,
					CommonConstants.LOG_LV_ERROR, null, MS_01, MS_02);
			return result;
		}
		return result;
	}
}
