package com.lastSchedule.repository;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SchoolMapperInterface {
    @Select(" select s.schoolName from schoolInfo as s where schoolName like '% #{name} %' ")
    List<String> searchBySchoolName(@Param("name") String name);
}

