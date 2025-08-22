package com.example.demo.presentation.CS03;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.SalseHistoryEntity;
import com.example.demo.entity.StatusEntity;
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
	public List<SalseHistoryEntity> initSales() {

		List<SalseHistoryEntity> callActionHistoryEntityList = salesService.salesListView();

		return callActionHistoryEntityList;
	}

	/*
	 * ステータス返却
	 */
	@PostMapping("/get-statslist")
	public List<StatusEntity> getStatsList(@RequestBody StatusEntity stats) {

		// ユーザー会社コード
		String companyCode = stats.getMycompanycode();

		List<StatusEntity> statsList = new ArrayList<StatusEntity>();

		statsList = salesService.getStatsList(companyCode);

		return statsList;
	}
}