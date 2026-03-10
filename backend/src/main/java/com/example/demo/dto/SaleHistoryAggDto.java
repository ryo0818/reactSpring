package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 営業履歴 時間別集計DTO
 * SQL結果: hour_time, status_name, cnt
 */
@Getter
@Setter
public class SaleHistoryAggDto {

    /** 時間（DATE_TRUNC結果） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime hourTime;

    /** ステータス名 */
    private String statusName;

    /** 件数 */
    private Integer cnt;
}
