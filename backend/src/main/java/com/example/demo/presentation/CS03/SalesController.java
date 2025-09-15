package com.example.demo.presentation.CS03;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
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

	@Autowired
	ApplicationLogger logger;

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
	 * @return 0(登録失敗) or 新規登録ID
	 */
	@PostMapping("/insert-salse")
	public String insertSalse(@RequestBody(required = false) SalesEntity sales) {

		// 会社コード
		String mycompanycode = sales.getMycompanycode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}
		
		// 登録日付に現在日時を設定する
		sales.setInsertdatetime(LocalDateTime.now());

		// 新規IDを設定する
		int id = salesService.getMaxId(CommonConstants.FLG_ON);

		// 取得したIDが0の場合は強制終了
		if (id == 0) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// IDを設定する
		sales.setId(String.valueOf(id));

		// 新規登録処理
		String result = salesService.insertSalse(sales);

		// 新規登録が失敗した場合は固定値「0」を返却する。
		if (CommonConstants.FLG_RESULT_FALSE.equals(result)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 登録成功の場合は登録したIDを返却する。
		result = sales.getId();

		return result;
	}

	/*
	 * 営業会社を更新する。
	 * @param SalseEntity 登録対象営業会社
	 * 
	 * @return 0(登録失敗) or 新規登録ID
	 */
	@PostMapping("/update-salse")
	public String updateSalse(@RequestBody(required = false) List<SalesEntity> saleslist) {

		// リスト型の更新営業リストをチェックする。
		for (SalesEntity sales : saleslist) {
			// IDまたは会社コードが存在しない場合は処理を終了する。
			if (!StringUtils.hasText(sales.getId()) || !StringUtils.hasText(sales.getMycompanycode())) {
				return CommonConstants.FLG_RESULT_FALSE;
			}
		}

		// ID重複チェック
		long distinct = saleslist.stream().map(SalesEntity::getId).distinct().count();
		if (distinct != saleslist.size()) {
			// ID重複エラーメッセージ追加
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9202, CommonConstants.LOG_LV_ERROR, null, "ID", "営業リスト");
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 営業リストを更新する
		String result = salesService.updateSalseById(saleslist);

		return result;
	}
}