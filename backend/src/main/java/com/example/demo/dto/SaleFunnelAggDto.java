package com.example.demo.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SaleFunnelAggDto {

    /** 集計単位の日時 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime aggregatedDateTime;

    /** 架電数（全ステータス合計: A+B+C+D+E+F+G+H） */
    private int callCount;

    /** 接続数（架電 - 現あな - 不通: A+B+C+D+E+F） */
    private int connectCount;

    /** オーナー数（A+B+C+D） */
    private int ownerCount;

    /** フル数（A+B） */
    private int fullCount;

    /** アポ数（A） */
    private int appointmentCount;

    /** A: アポ */
    private int apoCount;

    /** B: 再コールS */
    private int recallSCount;

    /** C: 再コールA */
    private int recallACount;

    /** D: 再コールNG */
    private int recallNgCount;

    /** E: 再コールB */
    private int recallBCount;

    /** F: 受付ブロック */
    private int blockCount;

    /** G: 現あな */
    private int genaCount;

    /** H: 不通 */
    private int futsuCount;
}
