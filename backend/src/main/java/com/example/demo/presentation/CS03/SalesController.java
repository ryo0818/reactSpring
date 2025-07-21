package com.example.demo.presentation.CS03;

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
		List<CallActionHistoryEntity> callActionHistoryEntityList = salesService.salesListView();

		return callActionHistoryEntityList;

	}
}