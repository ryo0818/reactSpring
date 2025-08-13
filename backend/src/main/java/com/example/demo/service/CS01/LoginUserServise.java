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
	public String cheackLoginUser(String userId, String email) {

		return userLoginRepository.cheackLoginUser(userId, email);
	}

	/*
	 * 会社コードチェック
	 */
	public String cheackCompanyCode(String companyCode) {

		return userLoginRepository.cheackCompanyCode(companyCode);
	}

}
