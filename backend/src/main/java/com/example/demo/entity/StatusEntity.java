package com.example.demo.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusEntity {
	/** ステータスコード */
	private String status;

	/** ステータス名 */
	private String statusName;

	/** 所属会社コード */
	private String mycompanycode;
}
