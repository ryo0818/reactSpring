package com.example.demo.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
	private LocalDateTime insertdatetime;

}
