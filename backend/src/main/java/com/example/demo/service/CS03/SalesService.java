package com.example.demo.service.CS03;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.entity.SalelistStatusHi;
import com.example.demo.entity.SalesEntity;
import com.example.demo.entity.StatusEntity;
import com.example.demo.repository.SaleRepository;
import com.example.demo.repository.StatsTableRepository;

@Service
public class SalesService {

	@Autowired
	SaleRepository saleRepository;

	@Autowired
	StatsTableRepository statsRepository;

	@Autowired
	ApplicationLogger logger;

	/*
	 * 営業情報から検索を行う
	 */
	public List<SalesEntity> getSalesSearch(SalesEntity sales) throws Exception {

		return saleRepository.getSalesSearch(sales);

	}

	/*
	 * ステータス返却
	 */
	public List<StatusEntity> getStatsList(String mycompanycode) throws Exception {

		return statsRepository.getStatsList(mycompanycode);
	}

	/*
	 * IDの最大を取得する。
	 * @Param resultFlg フラグOFF：IDの最大値,フラグON：IDの採番
	 * 
	 */
	public int getMaxId(String resultFlg) {

		// 営業テーブルからIDの最大値を取得する
		String result = saleRepository.getMaxId();

		int resltIdNum = 0;

		// フラグがOFFの場合はIDをそのまま返却する。
		if (CommonConstants.FLG_OFF.equals(resultFlg)) {
			resltIdNum = Integer.valueOf(result);
			return resltIdNum;
		}

		// フラグがONの場合は新しいIDを採番する
		if (CommonConstants.FLG_ON.equals(resultFlg)) {
			resltIdNum += Integer.valueOf(result) + 1;
			return resltIdNum;
		}

		return 0;
	}

	/*
	 * 営業会社を登録する。
	 */
	@Transactional
	public String insertSalse(SalesEntity sales) {

		// 登録結果
		int result = 0;

		try {
			// 登録処理実施
			result = saleRepository.insertSalse(sales);

			// 登録結果が0件の場合[結果:0]を返却する
			if (result == 0) {
				return CommonConstants.FLG_RESULT_FALSE;
			}

			// 営業会社ステータスを新規登録する
			result = insertSalseStats(sales);

			// 登録結果が0件の場合[結果:0]を返却する
			if (result == 0) {
				return CommonConstants.FLG_RESULT_FALSE;
			}

		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9201, CommonConstants.LOG_LV_ERROR, null, "ID", "営業情報");
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 登録結果が1件以上の場合[結果:1]を返却する
		return CommonConstants.FLG_RESULT_TRUE;
	}

	/*
	 * 営業リストの更新を行う
	 */
	@Transactional
	public String updateSalseById(List<SalesEntity> list) {

		// 更新結果
		int result = 0;

		try {
			// 同時更新件数ごとに更新処理を実施する
			for (int i = 0; i < list.size(); i += CommonConstants.CHUNK_SIZE) {
				// 同時更新・登録件数を取得する
				List<SalesEntity> chunk = list.subList(i, Math.min(i + CommonConstants.CHUNK_SIZE, list.size()));

				// 営業情報更新
				result += saleRepository.updateSalseById(chunk);
			}

			// 更新件数とデータが不一致の場合
			if (list.size() != result) {

				// エラーメッセージ・更新件数を出力する
				logger.outLogMessage(MessagesPropertiesConstants.LOG_9203, CommonConstants.LOG_LV_ERROR, null,
					String.valueOf(list.size()), String.valueOf(result));

				// 独自例外呼び出し
				throw new IllegalStateException();
			}
		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9201, CommonConstants.LOG_LV_ERROR, null, "ID", "営業情報");
			return CommonConstants.FLG_RESULT_FALSE;
		}
		return CommonConstants.FLG_RESULT_TRUE;
	}

	/*
	 * 営業会社ステータスを登録する
	 */
	@Transactional
	public int insertSalseStats(SalesEntity sales) throws DuplicateKeyException {

		// 登録結果
		int result = 0;

		// 会社ステータス
		SalelistStatusHi stats = new SalelistStatusHi();

		// ID
		stats.setId(sales.getId());
		
		// チームコード
		stats.setMyteamcode(sales.getMyteamcode());

		// ステータス
		stats.setStatus(sales.getStatus());

		// 登録日付
		stats.setInsertdatetime(sales.getInsertdatetime());

		// ステータス登録
		result = saleRepository.insertSaleStats(stats);

		return result;
	}
}
