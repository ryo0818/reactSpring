package com.example.demo.presentation.CS01;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.CS01.LoginUserServise;

@RestController
@RequestMapping("/login")
public class LoginUserController {

	@Autowired
	LoginUserServise loginUserServise;

	/*
	 * 会社コードを取得する
	 */
	@PostMapping("/company-code")
	public String loginCompanyCode(@RequestBody Map<String, Object> payload) {
		String userId = (String) payload.get("id");
		String mailAdder = (String) payload.get("email");
		String result = loginUserServise.cheakCompanyCode(userId, mailAdder);
		return result;
	}
}
