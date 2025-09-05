package com.example.demo.entity;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalelistStatusHi {

	/* レコードID */
	private String id;

	/* ステータス */
	private String status;

	/* 登録日付 */
	private LocalDateTime insertdatetime;

}
