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

		return userLoginRepository.getUserInfo(userId, mailAdder);
	}

	/*
	 * 新規登録
	 * 
	 */
	public int insertUser(RegUserEntity regUser) {

		return userLoginRepository.insertUser(regUser);
	}
}
