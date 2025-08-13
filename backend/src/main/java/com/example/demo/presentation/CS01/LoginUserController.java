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
	 * ログインユーザーを確認
	 */
	@PostMapping("/cheack-user")
	public String cheackLoginUser(@RequestBody Map<String, Object> payload) {
		String userId = (String) payload.get("id");
		String mailAdder = (String) payload.get("email");
		String result = loginUserServise.cheackLoginUser(userId, mailAdder);
		return result;
	}

	/*
	 * 会社コードを取得する
	 */
	@PostMapping("/cheack-cmpcode")
	public String cheackCompanyCode(@RequestBody Map<String, Object> payload) {
		String companyCode = (String) payload.get("companyCode");
		String result = loginUserServise.cheackCompanyCode(companyCode);
		return result;
	}
}
