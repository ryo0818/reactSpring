package com.example.demo.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {

	@GetMapping("/hello")
	public List<Map<String, Object>> hello() {

		return getCallLogs();
	}

	// JSONテスト
	public List<Map<String, Object>> getCallLogs() {
		return List.of(
			Map.of(
				"companyName", "株式会社ABC",
				"phoneNumber", "03-1234-5678",
				"callDate", "2025-07-03",
				"callCount", 2,
				"status", "対応中",
				"personInCharge", "田中",
				"note", "次回は担当者変更予定"),
			Map.of(
				"companyName", "XYZコンサル",
				"phoneNumber", "06-9876-5432",
				"callDate", "2025-07-02",
				"callCount", 1,
				"status", "完了",
				"personInCharge", "佐藤",
				"note", ""));
	}
}
