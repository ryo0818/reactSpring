package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StatusCountDto {
    /** 表示ラベル（SQLの時間単位に応じた値） */
    private String label;

    /** ステータスレベル（1:架電 2:接続 3:オーナ 4:フル 5:アポ） */
    private Integer statusId;

    /** 件数 */
    private Integer statusCount;
}
