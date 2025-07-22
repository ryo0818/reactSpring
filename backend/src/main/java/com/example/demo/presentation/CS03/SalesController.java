package com.example.demo.presentation.CS03;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.CallActionHistoryEntity;
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
	 * CS0301 初期表示処理 営業リスト表示
	 */
	@PostMapping("/list-view")
	public List<CallActionHistoryEntity> initSales() {

		// 初期表示
		//		List<CallActionHistoryEntity> callActionHistoryEntityList = salesService.salesListView();

		List<CallActionHistoryEntity> callActionHistoryEntityList = new ArrayList<CallActionHistoryEntity>();
		callActionHistoryEntityList.add(create("A004", "U004", "XYZコンサルティング", "045-4567-8901", "2025-07-06", 2, "B", "山田", "321321"));
		callActionHistoryEntityList.add(create("A003", "U003", "テスト商事株式会社", "052-3456-7890", "2025-07-05", 1, "C", "佐藤", null));
		callActionHistoryEntityList.add(create("A002", "U002", "ABC株式会社", "06-2345-6789", "2025-07-04", 1, "B", "鈴木", "ｆｄさｆｄさｆｄさ"));
		callActionHistoryEntityList.add(create("A005", "U005", "Techソリューションズ", "092-5678-9012", "2025-07-03", 1, "C", "中村", "dsadsa"));
		callActionHistoryEntityList.add(create("A001", "U001", "株式会社サンプル", "03-1234-5678", "2025-07-03", 1, "C", "田中", "ｄさｇ"));
		callActionHistoryEntityList.add(create("A003", "U003", "テスト商事株式会社", "052-3456-7890", "2025-07-02", 3, "A", "佐藤", "１２３４５６７８９０１２３４５６７８９０..." /* 長文省略可 */));
		callActionHistoryEntityList.add(create("A001", "U001", "株式会社サンプル", "03-1234-5678", "2025-07-02", 2, "B", "田中", "めも２"));
		callActionHistoryEntityList.add(create("A004", "U004", "XYZコンサルティング", "045-4567-8901", "2025-07-01", 1, "A", "山田", "ｄさ"));
		callActionHistoryEntityList.add(create("A001", "U001", "株式会社サンプル", "03-1234-5678", "2025-07-01", 1, "A", "田中", "メモ１"));
		callActionHistoryEntityList.add(create("A002", "U002", "ABC株式会社", "06-2345-6789", "2025-07-01", 1, "A", "鈴木", "ｇｆだｇｒ"));

		return callActionHistoryEntityList;
	}

	private CallActionHistoryEntity create(String orgCode, String userId, String companyName, String phoneNumber,
		String callDate, int callCount, String status,
		String staffName, String note) {
		CallActionHistoryEntity entity = new CallActionHistoryEntity();
		entity.setOrgCode(orgCode);
		entity.setUserId(userId);
		entity.setCompanyName(companyName);
		entity.setPhoneNumber(phoneNumber);
		entity.setCallDate(LocalDate.parse(callDate));
		entity.setCallCount(callCount);
		entity.setStatus(status);
		entity.setStaffName(staffName);
		entity.setNote(note);
		return entity;
	}
}