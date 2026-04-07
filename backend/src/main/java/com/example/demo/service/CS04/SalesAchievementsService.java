package com.example.demo.service.CS04;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SaleFunnelAggDto;
import com.example.demo.dto.SaleHistoryAggDto;
import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.repository.SaleHistoryRepository;

@Service
public class SalesAchievementsService {

	@Autowired
	SaleHistoryRepository saleHistoryRepository;

	/**
	 * 時間別集計結果を返す。
	 */
	public List<SaleHistoryAggDto> salesAchievement(SalesAchievementsDto sale) {
		return saleHistoryRepository.selectAggregatedByUnit(sale);
	}

	/**
	 * 時間単位ごとにファネル集計を行う。
	 */
	public List<SaleFunnelAggDto> aggregateFunnel(List<SaleHistoryAggDto> resultList) {

		// aggregatedDateTime をキーに挿入順を保持したMapでグルーピング
		Map<LocalDateTime, SaleFunnelAggDto> funnelMap = new LinkedHashMap<>();

		for (SaleHistoryAggDto dto : resultList) {
			SaleFunnelAggDto funnel = funnelMap.computeIfAbsent(dto.getAggregatedDateTime(), dt -> {
				SaleFunnelAggDto f = new SaleFunnelAggDto();
				f.setAggregatedDateTime(dt);
				return f;
			});

			int count = dto.getSalesCount() != null ? dto.getSalesCount() : 0;
			switch (dto.getStatusName()) {
				case "アポ"        -> funnel.setApoCount(funnel.getApoCount() + count);
				case "再コールS"   -> funnel.setRecallSCount(funnel.getRecallSCount() + count);
				case "再コールA"   -> funnel.setRecallACount(funnel.getRecallACount() + count);
				case "再コールNG"  -> funnel.setRecallNgCount(funnel.getRecallNgCount() + count);
				case "再コールB"   -> funnel.setRecallBCount(funnel.getRecallBCount() + count);
				case "受付ブロック" -> funnel.setBlockCount(funnel.getBlockCount() + count);
				case "現あな"      -> funnel.setGenaCount(funnel.getGenaCount() + count);
				case "不通"        -> funnel.setFutsuCount(funnel.getFutsuCount() + count);
				default            -> { /* 不明ステータスは無視 */ }
			}
		}

		// ファネル計算式を各時間単位に適用する
		for (SaleFunnelAggDto funnel : funnelMap.values()) {
			
			// ファネルの各段階の数値を取得
			int apo     = funnel.getApoCount();
			int recallS = funnel.getRecallSCount();
			int recallA = funnel.getRecallACount();
			int recallNg = funnel.getRecallNgCount();
			int recallB = funnel.getRecallBCount();
			int block   = funnel.getBlockCount();
			int gena    = funnel.getGenaCount();
			int futsu   = funnel.getFutsuCount();

			// ファネルの各段階の数値を計算式に基づいて設定
			funnel.setCallCount(apo + recallS + recallA + recallNg + recallB + block + gena + futsu);
			funnel.setConnectCount(apo + recallS + recallA + recallNg + recallB + block);
			funnel.setOwnerCount(apo + recallS + recallA + recallNg);
			funnel.setFullCount(apo + recallS);
			funnel.setAppointmentCount(apo);
		}

		return new ArrayList<>(funnelMap.values());
	}
}
