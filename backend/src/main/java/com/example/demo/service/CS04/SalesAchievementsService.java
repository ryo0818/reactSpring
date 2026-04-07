package com.example.demo.service.CS04;

import java.util.List;

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
	 * ファネル集計を行う。
	 */
	public SaleFunnelAggDto aggregateFunnel(List<SaleHistoryAggDto> resultList) {

		SaleFunnelAggDto funnel = new SaleFunnelAggDto();

		for (SaleHistoryAggDto dto : resultList) {
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

		int apo     = funnel.getApoCount();
		int recallS = funnel.getRecallSCount();
		int recallA = funnel.getRecallACount();
		int recallNg = funnel.getRecallNgCount();
		int recallB = funnel.getRecallBCount();
		int block   = funnel.getBlockCount();
		int gena    = funnel.getGenaCount();
		int futsu   = funnel.getFutsuCount();

		funnel.setCallCount(apo + recallS + recallA + recallNg + recallB + block + gena + futsu);
		funnel.setConnectCount(apo + recallS + recallA + recallNg + recallB + block);
		funnel.setOwnerCount(apo + recallS + recallA + recallNg);
		funnel.setFullCount(apo + recallS);
		funnel.setAppointmentCount(apo);

		return funnel;
	}
}
