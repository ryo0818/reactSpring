package com.example.demo.presentation.CS02;

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
import com.example.demo.dto.SalesClientDto;
import com.example.demo.entity.SalesEntity;
import com.example.demo.service.CS02.SalesService;

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
	 * 
	 * @return 営業履歴リストを表示
	 */
	@PostMapping("/list-view")
	public List<SalesEntity> getSalesSearch(
			@RequestBody(required = false) SalesClientDto sale) throws Exception {

		List<SalesEntity> resultSalseList = new ArrayList<SalesEntity>();

		// ユーザー会社コード
		String userCompanyCode = sale.getUserCompanyCode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(userCompanyCode)) {
			return resultSalseList;
		}

		// 営業履歴から検索を行う
		resultSalseList = salesService.getSalesSearch(sale);

		return resultSalseList;
	}

	/*
	 * 営業会社を登録する。
	 * @param SalseEntity 登録対象営業会社
	 * 
	 * @return 0(登録失敗) or 新規登録ID
	 */
	@PostMapping("/insert-salse")
	public String insertSalse(@RequestBody(required = false) SalesClientDto sales) {

		// 会社コード
		String mycompanycode = sales.getUserCompanyCode();

		// 会社コードが存在しない場合は処理を終了する。
		if (!StringUtils.hasText(mycompanycode)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 登録日付に現在日時を設定する
		sales.setInsertDateTime(LocalDateTime.now());

		// 新規IDを設定する
		int id = salesService.getMaxId(CommonConstants.FLG_ON);

		// 取得したIDが0の場合は強制終了
		if (id == 0) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// IDを設定する
		sales.setSaleId(String.valueOf(id));

		// 新規登録処理
		String result = salesService.insertSalse(sales);

		// 新規登録が失敗した場合は固定値「0」を返却する。
		if (CommonConstants.FLG_RESULT_FALSE.equals(result)) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 登録成功の場合は登録したIDを返却する。
		result = sales.getSaleId();

		return result;
	}

	/*
	 * 営業会社を更新する。
	 * @param SalseEntity 登録対象営業会社
	 * 
	 * @return 0(登録失敗) or 新規登録ID
	 */
	@PostMapping("/update-salse")
	public String updateSalse(@RequestBody(required = false) List<SalesClientDto> saleslist) {

		// 新規IDを設定する
		int id = salesService.getMaxId(CommonConstants.FLG_ON);

		// 取得したIDが0の場合は強制終了
		if (id == 0) {
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// リスト型の更新営業リストをチェックする。
		for (SalesClientDto sales : saleslist) {
			// IDまたは会社コードが存在しない場合は処理を終了する。
			if (!StringUtils.hasText(sales.getUserId()) || !StringUtils.hasText(sales.getUserCompanyCode())) {

				return CommonConstants.FLG_RESULT_FALSE;
			}

			// IDを設定する
			sales.setSaleId(String.valueOf(id++));
			
			// 登録日付に現在日時を設定する
			sales.setInsertDateTime(LocalDateTime.now());
			
		}

		// ID重複チェック
		long distinct = saleslist.stream().map(SalesClientDto::getSaleId).distinct().count();
		if (distinct != saleslist.size()) {
			// ID重複エラーメッセージ追加
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9202, CommonConstants.LOG_LV_ERROR, null, "ID",
					"営業リスト");
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 営業リストを更新する
		String result = salesService.updateSaleBySaleId(saleslist);

		return result;
	}

	/*
	 * 営業の登録を行うCSV
	 */
	@PostMapping("/isnert-salse-csv")
	public String insertSalseLsitCsv(@RequestBody(required = false) List<SalesClientDto> saleslist) {

		// リスト型の登録営業リストをチェックする。
		for (SalesClientDto sales : saleslist) {
			// IDまたは会社コードが存在しない場合は処理を終了する。
			if (!StringUtils.hasText(sales.getUserId()) || !StringUtils.hasText(sales.getUserCompanyCode())) {

				return CommonConstants.FLG_RESULT_FALSE;
			}

			// 登録日付に現在日時を設定する
			sales.setInsertDateTime(LocalDateTime.now());
		}

		// ID重複チェック
		long distinct = saleslist.stream().map(SalesClientDto::getSaleId).distinct().count();
		if (distinct != saleslist.size()) {
			// ID重複エラーメッセージ追加
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9202, CommonConstants.LOG_LV_ERROR, null, "ID",
					"営業リスト");
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 営業リストを更新する
		int result = salesService.insertSalseList(saleslist);

		return String.valueOf(result);
	}
}