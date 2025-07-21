package com.example.demo.presentation.CS01;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloReactController {

	@GetMapping("/api/hello")
	public String initHelloReact() {

		return "処理開始";

	}
}
