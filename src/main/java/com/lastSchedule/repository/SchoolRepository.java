package com.lastSchedule.repository;

import com.lastSchedule.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {

    School findBySchoolCode(String schoolcode);
}
