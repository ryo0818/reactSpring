package com.example.demo.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.entity.StatusEntity;

@Mapper
public interface StatsTableRepository {
	List<StatusEntity> getStatsList(@Param("mycompanycode") String companycode);
}
