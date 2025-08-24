package com.example.demo.service.CS03;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
