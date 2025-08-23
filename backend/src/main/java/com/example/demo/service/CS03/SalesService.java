package com.example.demo.service.CS03;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.SalseHistoryEntity;
import com.example.demo.entity.StatusEntity;
import com.example.demo.repository.SaleHistoryRepository;
import com.example.demo.repository.StatsTableRepository;

@Service
public class SalesService {

	@Autowired
	SaleHistoryRepository saleHistoryRepository;

	@Autowired
	StatsTableRepository statsRepository;

	/*
	 * CS0301 初期表示処理
	 */
	public List<SalseHistoryEntity> getSalesHistorySearch(SalseHistoryEntity salseHostory) throws Exception {

		return saleHistoryRepository.getSalesHistorySearch();

	}

	/*
	 * ステータス返却
	 */
	public List<StatusEntity> getStatsList(String mycompanycode) throws Exception {

		return statsRepository.getStatsList(mycompanycode);
	}
}
