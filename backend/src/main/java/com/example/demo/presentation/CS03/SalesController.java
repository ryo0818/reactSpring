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

import com.example.demo.entity.SalseHistoryEntity;
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
	public List<SalseHistoryEntity> getSalesHistorySearch(
		@RequestBody(required = false) SalseHistoryEntity salseHistory,
		HttpSession session) throws Exception {

		List<SalseHistoryEntity> resultSalseHistoryList = new ArrayList<SalseHistoryEntity>();

		// セッションからユーザー情報を取得
		UserSessionEntity userSession = (UserSessionEntity) session.getAttribute(UserSessionInfo.ATTR_USER);

		// ユーザー会社コード
		String mycompanycode = userSession.getMycompanycode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return resultSalseHistoryList;
		}

		// 営業履歴から検索を行う
		resultSalseHistoryList = salesService.getSalesHistorySearch(salseHistory);

		return resultSalseHistoryList;
	}

	/*
	 * ステータス返却
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
}