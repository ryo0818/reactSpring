package com.example.demo.presentation.CS01;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.RegUserEntity;
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
	public RegUserEntity cheackLoginUser(@RequestBody Map<String, Object> payload) {

		// ユーザーID
		String userId = (String) payload.get("id");

		// メールアドレス
		String mailAdder = (String) payload.get("email");

		// ユーザー情報確認
		RegUserEntity result = loginUserServise.getUserInfo(userId, mailAdder);

		return result;
	}

	//	/*
	//	 * 会社コードを取得する
	//	 */
	//	@PostMapping("/cheack-cmpcode")
	//	public RegUserEntity cheackCompanyCode(@RequestBody Map<String, Object> payload) {
	//		String companyCode = (String) payload.get("companyCode");
	//		RegUserEntity result = loginUserServise.cheackCompanyCode(companyCode);
	//		return result;
	//	}

	/*
	 * 新規登録
	 */
	@PostMapping("/insert-user")
	public int insertUser(@RequestBody RegUserEntity regUser) {
		int result = loginUserServise.insertUser(regUser);
		return result;

	}
}
