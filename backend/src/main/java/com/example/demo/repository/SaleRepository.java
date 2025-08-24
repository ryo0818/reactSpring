package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.entity.SalseEntity;

@Mapper
public interface SaleRepository {
	List<SalseEntity> getSalesSearch();
}
