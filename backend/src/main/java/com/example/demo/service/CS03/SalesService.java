package com.example.demo.service.CS03;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.entity.SalseEntity;
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
	public List<SalseEntity> getSalesSearch(SalseEntity salseHostory) throws Exception {

		return saleHistoryRepository.getSalesSearch();

	}

	/*
	 * ステータス返却
	 */
	public List<StatusEntity> getStatsList(String mycompanycode) throws Exception {

		return statsRepository.getStatsList(mycompanycode);
	}

	/*
	 * リスト登録
	 */
	public String insertSalse(SalseEntity salseHistory) {

		// 登録結果
		int result = 0;

		try {
			// 登録処理実施
			result = saleHistoryRepository.insertSalse(salseHistory);

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
}
