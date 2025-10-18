package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusEntity {

	/** ステータス名 */
	private String statusName;

	/** ステータスレベル */
	private Integer statusLevel;

	/** ステータスID */
	private Integer statusId;
	/** 所属会社コード */
	private String userCompanyCode;
}
