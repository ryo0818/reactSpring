package com.example.demo.presentation.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
	public String initLogin(@RequestParam("id") String userId,
		@RequestParam("email") String email) {
		String result = loginUserServise.cheakCompanyCode(userId, email);
		return result;
	}
}
