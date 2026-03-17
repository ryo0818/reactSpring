package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

/**
 * 営業履歴 集計DTO
 * SQL結果: aggregated_date_time, status_name, sales_count
 */
@Getter
@Setter
public class SaleHistoryAggDto {

    /** 集計単位の日時（DATE_TRUNC結果） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime aggregatedDateTime;

    /** ステータス名 */
    private String statusName;

    /** 件数 */
    private Integer salesCount;
}
