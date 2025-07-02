package com.example.demo;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class HelloController {
	@GetMapping("/hello")
	public String hello() {

		// 文字表示
		String sampleDtring = "動作確認とプッシュテスト。　　";

		return sampleDtring;
	}
}
