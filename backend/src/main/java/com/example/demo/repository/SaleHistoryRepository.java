package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.SalseHistoryEntity;

@Mapper
public interface SaleHistoryRepository {
	List<SalseHistoryEntity> getSalesHistorySearch();
}
