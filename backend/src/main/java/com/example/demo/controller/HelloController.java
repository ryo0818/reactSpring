package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
	@GetMapping("/hello")
	public String hello() {

		// 文字表示
		String sampleDtring = "ポート番号設定テスト";

		return sampleDtring;
	}
}
