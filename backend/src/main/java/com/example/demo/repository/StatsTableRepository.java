package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.StatusEntity;

@Mapper
public interface StatsTableRepository {

	// ステータス一覧を取得する
	List<StatusEntity> getStatsList(@Param("userCompanyCode") String userCompanyCode);
}
