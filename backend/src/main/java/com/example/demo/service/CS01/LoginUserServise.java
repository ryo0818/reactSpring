package com.example.demo.service.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.repository.UserLoginRepository;

@Service
public class LoginUserServise {

	@Autowired
	UserLoginRepository userLoginRepository;

	/*
	 * 会社コードチェック
	 */
	public String cheakCompanyCode(String userId, String email) {

		return userLoginRepository.cheakUserLogin(userId, email);
	}

}
