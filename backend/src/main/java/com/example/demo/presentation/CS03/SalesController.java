package com.example.demo.presentation.CS03;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.constats.CommonConstants;
import com.example.demo.entity.SalesEntity;
import com.example.demo.entity.StatusEntity;
import com.example.demo.service.CS03.SalesService;

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
	public List<SalesEntity> getSalesSearch(
		@RequestBody(required = false) SalesEntity salseHistory) throws Exception {

		List<SalesEntity> resultSalseHistoryList = new ArrayList<SalesEntity>();

		// セッションからユーザー情報を取得
		// UserSessionEntity userSession = (UserSessionEntity) session.getAttribute(UserSessionInfo.ATTR_USER);

		// ユーザー会社コード
		String mycompanycode = salseHistory.getMycompanycode();

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
	public List<StatusEntity> getStatsList(@RequestBody(required = false) SalesEntity salseHistory) throws Exception {

		// ユーザー会社コード
		String mycompanycode = salseHistory.getMycompanycode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return List.of();
		}

		// ユーザー所属会社が使用しているステータスを取得する。
		List<StatusEntity> resultStatsList = salesService.getStatsList(mycompanycode);

		return resultStatsList;
	}

	/*
	 * 営業会社を登録する。
	 * @param SalseEntity 登録対象営業会社
	 * 
	 * @return 0 or 1 0:登録無し。1:登録
	 */
	@PostMapping("/insert-salse")
	public String insertSalse(@RequestBody(required = false) SalesEntity sales) {

		// 会社コード
		String mycompanycode = sales.getMycompanycode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return CommonConstants.FLG_ZERO;
		}

		// 新規IDを設定する
		int id = salesService.getMaxId(CommonConstants.FLG_ONE);

		// IDを設定する
		sales.setId(String.valueOf(id));

		// 登録処理
		String result = salesService.insertSalse(sales);

		// 登録結果がOKの場合、履歴テーブルに履歴を登録する
		if (CommonConstants.FLG_ONE.equals(result)) {
			try {
				salesService.insertSalseStats(sales);
			} catch (Exception e) {
				// TODO 自動生成された catch ブロック
				e.printStackTrace();
			}
		}

		return String.valueOf(id);
	}

	/*
	 * 営業の最新
	 */

	/*
	 * 営業リストを更新する。
	 * @param SalseEntity 更新対象営業会社
	 * 
	 * @return 0 or 1 0:登録無し。1:登録
	 */

}