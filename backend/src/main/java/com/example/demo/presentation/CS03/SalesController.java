package com.example.demo.presentation.CS03;

import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constats.CommonConstants;
import com.example.demo.entity.SalseEntity;
import com.example.demo.entity.StatusEntity;
import com.example.demo.service.CS03.SalesService;
import com.example.demo.session.UserSessionEntity;
import com.example.demo.session.UserSessionInfo;

/*
 * 営業リストコントローラー
 */
@RestController
@RequestMapping("/sales")
public class SalesController {

	/** 営業リストサービスクラス*/
	@Autowired
	SalesService salesService;

	/*
	 * 検索対象の営業リストを表示する
	 * @param mycompanycode 所属会社コード
	 * @param industry 業界
	 * @param companyName 会社名
	 * @param phoneNumber 電話番号
	 * @param callDate 架電日
	 * @param callCount 架電数
	 * @param status ステータス
	 * @param staff 担当者
	 * @param url 会社URL
	 * @param address 住所
	 * @param remarks 備考
	 * 
	 * @return 営業履歴リストを表示
	 * 
	 */
	@PostMapping("/list-view")
	public List<SalseEntity> getSalesSearch(
		@RequestBody(required = false) SalseEntity salseHistory,
		HttpSession session) throws Exception {

		List<SalseEntity> resultSalseHistoryList = new ArrayList<SalseEntity>();

		// セッションからユーザー情報を取得
		UserSessionEntity userSession = (UserSessionEntity) session.getAttribute(UserSessionInfo.ATTR_USER);

		// ユーザー会社コード
		String mycompanycode = userSession.getMycompanycode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return resultSalseHistoryList;
		}

		// 営業履歴から検索を行う
		resultSalseHistoryList = salesService.getSalesSearch(salseHistory);

		return resultSalseHistoryList;
	}

	/*
	 * ステータス一覧返却
	 * 
	 * @return ステータス
	 */
	@PostMapping("/get-statslist")
	public List<StatusEntity> getStatsList(HttpSession session) throws Exception {

		// ユーザー会社コード
		String mycompanycode = UserSessionInfo.getMycompanycode(session);

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return List.of();
		}

		// ユーザー所属会社が使用しているステータスを取得する。
		List<StatusEntity> resultStatsList = salesService.getStatsList(mycompanycode);

		return resultStatsList;
	}

	/*
	 * 営業リストを登録する。
	 * @param SalseEntity 登録対象営業会社
	 * 
	 * @return 0 or 1 0:登録無し。1:登録
	 */
	@PostMapping("/add-salse")
	public String insertSalse(@RequestBody(required = false) SalseEntity salseHistory, HttpSession session) {

		// セッション・会社コード
		String mycompanycode = UserSessionInfo.getMycompanycode(session);

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return CommonConstants.FLG_ZERO;
		}

		// 連携された会社コードと比較し、不一致の場合は返却
		if (!mycompanycode.equals(salseHistory.getMycompanycode())) {
			return CommonConstants.FLG_ZERO;
		}

		// 登録処理
		String result = salesService.insertSalse(salseHistory);

		return result;
	}
}