package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.example.demo.model.CallActionHistoryEntity;

@Mapper
public interface CallActionHistoryRepository {

	List<CallActionHistoryEntity> selectAll();
}
