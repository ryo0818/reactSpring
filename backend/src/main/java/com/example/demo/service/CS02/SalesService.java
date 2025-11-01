package com.example.demo.service.CS02;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.config.ApplicationLogger;
import com.example.demo.constats.CommonConstants;
import com.example.demo.constats.MessagesPropertiesConstants;
import com.example.demo.dto.SalesClientDto;
import com.example.demo.entity.SaleHistoryEntity;
import com.example.demo.entity.SalesEntity;
import com.example.demo.repository.SaleRepository;

@Service
public class SalesService {

	@Autowired
	SaleRepository saleRepository;

	@Autowired
	ApplicationLogger logger;

	/*
	 * 営業情報から検索を行う
	 */
	public List<SalesEntity> getSalesSearch(SalesClientDto sales) throws Exception {

		// 営業情報
		SalesEntity salesEntity = new SalesEntity();

		// 入力情報を営業リストに設定する。
		BeanUtils.copyProperties(sales, salesEntity);

		// 検索処理
		List<SalesEntity> result = new ArrayList<SalesEntity>();

		// 検索結果を取得する
		result = saleRepository.getSalesSearch(salesEntity);

		return result;

	}

	/*
	 * IDの最大を取得する。
	 * @Param resultFlg フラグOFF：IDの最大値,フラグON：IDの採番
	 * 
	 */
	public int getMaxId(String resultFlg) {

		// 営業テーブルからIDの最大値を取得する
		String result = saleRepository.getMaxSalesId();

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
	public String insertSalse(SalesClientDto sales) {

		// 登録結果
		int result = 0;

		// 営業情報
		SalesEntity salesEntity = new SalesEntity();

		// 入力情報を営業リストに設定する。
		BeanUtils.copyProperties(sales, salesEntity);
		
		try {
			// 登録処理実施
			result = saleRepository.insertSale(salesEntity);

			// 登録結果が0件の場合[結果:0]を返却する
			if (result == 0) {
				return CommonConstants.FLG_RESULT_FALSE;
			}

			// 営業会社ステータスを新規登録する
			result = insertSalseStats(salesEntity);

			// 登録結果が0件の場合[結果:0]を返却する
			if (result == 0) {
				return CommonConstants.FLG_RESULT_FALSE;
			}

		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9201, CommonConstants.LOG_LV_ERROR, null, "ID",
					"営業情報");
			return CommonConstants.FLG_RESULT_FALSE;
		}

		// 登録結果が1件以上の場合[結果:1]を返却する
		return CommonConstants.FLG_RESULT_TRUE;
	}
	
	@Transactional
	public int insertSalseList(List<SalesClientDto> calesClientDtoList) {
		
		// 営業会社登録用
		List<SalesEntity> salesEntityList = new ArrayList<SalesEntity>();
		
	    // DTO → Entity の変換（1件ずつコピー）
	    for (SalesClientDto dto : calesClientDtoList) {
	        SalesEntity entity = new SalesEntity();
	        BeanUtils.copyProperties(dto, entity); // ←ここでOK！
	        salesEntityList.add(entity);
	    }
		
		// 登録処理実施
		int result = saleRepository.insertSalesList(salesEntityList);
		
		
		// 営業会社ステータスを新規登録する
		 for (SalesEntity salesEntity : salesEntityList) {
			 result = insertSalseStats(salesEntity);
		 }
		return result;
	}


	/*
	 * 営業リストの更新を行う
	 */
	@Transactional
	public String updateSaleBySaleId(List<SalesClientDto> salesDtoList) {

		// 更新結果
		int result = 0;

		// 営業情報リストに入力値を設定する。
		List<SalesEntity> salesEntityList = salesDtoList.stream()
				.map(dto -> {
					SalesEntity entity = new SalesEntity();
					BeanUtils.copyProperties(dto, entity);
					return entity;
				})
				.toList();

		try {
			// 同時更新件数ごとに更新処理を実施する
			for (int i = 0; i < salesEntityList.size(); i += CommonConstants.CHUNK_SIZE) {

				// 同時更新・登録件数を取得する
				List<SalesEntity> chunk = salesEntityList.subList(i,
						Math.min(i + CommonConstants.CHUNK_SIZE, salesEntityList.size()));

				// 営業情報更新
				result += saleRepository.updateSaleBySaleId(chunk);
			}

			// 更新件数とデータが不一致の場合
			if (salesEntityList.size() != result) {

				// エラーメッセージ・更新件数を出力する
				logger.outLogMessage(MessagesPropertiesConstants.LOG_9203, CommonConstants.LOG_LV_ERROR, null,
						String.valueOf(salesEntityList.size()), String.valueOf(result));

				// 独自例外呼び出し
				throw new IllegalStateException();
			}

			// 営業会社ステータスを新規登録する
			for (SalesEntity sales : salesEntityList) {
				// 営業会社ステータスを新規登録する
				result = insertSalseStats(sales);
			}

		} catch (DuplicateKeyException e) {
			// ログメッセージ：重複キーを設定
			logger.outLogMessage(MessagesPropertiesConstants.LOG_9201, CommonConstants.LOG_LV_ERROR, null, "ID",
					"営業情報");
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
		SaleHistoryEntity stats = new SaleHistoryEntity();

		// ID
		stats.setSaleId(sales.getSaleId());

		// チームコード
		stats.setUserTeamCode(sales.getUserCompanyCode());

		// ステータス
		stats.setStatusName(sales.getStatusName());

		// 登録日付
		stats.setInsertDateTime(sales.getInsertDateTime());

		// 有効フラグ
		stats.setValidFlg(false);

		// ステータスID
		stats.setStatusId(sales.getStatusId());

		// ステータス登録
		result = saleRepository.insertSaleStats(stats);

		return result;
	}
}
