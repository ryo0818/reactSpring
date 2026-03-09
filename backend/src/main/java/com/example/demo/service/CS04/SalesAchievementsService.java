package com.example.demo.service.CS04;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.SalesAchievementsDto;
import com.example.demo.dto.StatusCountDto;
import com.example.demo.repository.SaleHistoryRepository;

@Service
public class SalesAchievementsService {

	// status_level の値定義
	private static final int STATUS_CALL        = 1; // 架電
	private static final int STATUS_CONNECT     = 2; // 接続
	private static final int STATUS_OWNER       = 3; // オーナ
	private static final int STATUS_FULL        = 4; // フル
	private static final int STATUS_APPOINTMENT = 5; // アポ

	@Autowired
	SaleHistoryRepository saleHistoryRepository;

	/**
	 * 時間単位・会社コード・日付範囲に基づき営業実績を集計して返す。
	 * SQLの結果 (label, status_level, count) をラベル単位にピボットする。
	 */
	public List<SalesAchievementsDto> salesAchievement(SalesAchievementsDto sale) {

		List<StatusCountDto> rawData = saleHistoryRepository.selectAggregatedByUnit(sale);

		// ラベルをキーにして集計（LinkedHashMap でSQL ORDER BY の順序を保持）
		Map<String, SalesAchievementsDto> resultMap = new LinkedHashMap<>();

		for (StatusCountDto row : rawData) {

			if (row.getLabel() == null || row.getStatusId() == null) {
				continue;
			}

			SalesAchievementsDto dto = resultMap.computeIfAbsent(row.getLabel(), label -> {
				SalesAchievementsDto d = new SalesAchievementsDto();
				d.setLabel(label);
				return d;
			});

			int count = row.getStatusCount() != null ? row.getStatusCount() : 0;

			switch (row.getStatusId()) {
				case STATUS_CALL:
					dto.setCallCount(dto.getCallCount() + count);
					break;
				case STATUS_CONNECT:
					dto.setConnectCount(dto.getConnectCount() + count);
					break;
				case STATUS_OWNER:
					dto.setOwnerCount(dto.getOwnerCount() + count);
					break;
				case STATUS_FULL:
					dto.setFullCount(dto.getFullCount() + count);
					break;
				case STATUS_APPOINTMENT:
					dto.setAppointmentCount(dto.getAppointmentCount() + count);
					break;
				default:
					break;
			}
		}

		return new ArrayList<>(resultMap.values());
	}
}
