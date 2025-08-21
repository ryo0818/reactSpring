package com.example.demo.service.CS01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.RegUserEntity;
import com.example.demo.repository.UserLoginRepository;

@Service
public class LoginUserServise {

	@Autowired
	UserLoginRepository userLoginRepository;

	/*
	 * ユーザー情報確認
	 */
	public RegUserEntity getUserInfo(String userId, String mailAdder) {

		RegUserEntity result = new RegUserEntity();

		result = userLoginRepository.getUserInfo(userId, mailAdder);

		if (result == null) {
			result = new RegUserEntity();
			System.out.println("0ken");
			result.setResultStatus(false);
		} else {
			result.setResultStatus(true);
		}

		return result;
	}

	/*
	 * 新規登録
	 * 
	 */
	public int insertUser(RegUserEntity regUser) {

		return userLoginRepository.insertUser(regUser);
	}
}
