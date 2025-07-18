package com.example.demo.service.CS03;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.CallActionHistoryEntity;
import com.example.demo.repository.CallActionHistoryRepository;

@Service
public class SalesService {

	@Autowired
	CallActionHistoryRepository callActionHistoryRepository;

	/*
	 * CS0301 初期表示処理
	 */
	public List<CallActionHistoryEntity> salesListView() {

		return callActionHistoryRepository.selectAll();

	}
}
