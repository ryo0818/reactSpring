package com.example.demo.service.CS03;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

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
	SaleRepository saleHistoryRepository;

	@Autowired
	StatsTableRepository statsRepository;

	@Autowired
	ApplicationLogger logger;

	/*
	 * 営業履歴から検索を行う
	 */
	public List<SalesEntity> getSalesSearch(SalesEntity sales) throws Exception {

		return saleHistoryRepository.getSalesSearch(sales);

	}

	/*
	 * ステータス返却
	 */
	public List<StatusEntity> getStatsList(String mycompanycode) throws Exception {

		return statsRepository.getStatsList(mycompanycode);
	}

	/*
	 * IDの最大を取得する。
	 */
	public int getMaxId(String resultFlg) {

		String result = saleHistoryRepository.getMaxId();

		int resltIdNum = 0;

		// フラグがOFFの場合はIDをそのまま返却する。
		if (CommonConstants.FLG_ZERO.equals(resultFlg)) {
			resltIdNum = Integer.valueOf(result);
			return resltIdNum;
		}

		// フラグがONの場合は新しいIDを採番する
		if (CommonConstants.FLG_ONE.equals(resultFlg)) {
			resltIdNum += Integer.valueOf(result) + 1;
			return resltIdNum;
		}

		return 0;
	}

	/*
	 * 営業会社を登録する。
	 */
	public String insertSalse(SalesEntity sales) {

		// 登録結果
		int result = 0;

		try {
			// 登録処理実施
			result = saleHistoryRepository.insertSalse(sales);

			// 登録結果が0件の場合[結果:0]を返却する
			if (result == 0) {
				return CommonConstants.FLG_ZERO;
			}

		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_2001, CommonConstants.LOG_LV_DEBUG, null, (String[]) null);
			return CommonConstants.FLG_ZERO;
		}

		// 登録結果が1件以上の場合[結果:1]を返却する
		return CommonConstants.FLG_ONE;
	}

	/*
	 * 営業会社ステータスを登録する
	 */
	public int insertSalseStats(SalesEntity sales) throws Exception {

		// 登録結果
		int result = 0;

		SalelistStatusHi stats = new SalelistStatusHi();

		stats.setId(sales.getId());
		stats.setStatus(sales.getStatus());
		stats.setInsertdatetime(LocalDateTime.now());

		saleHistoryRepository.setSaleStats(stats);

		return 0;
	}
}
