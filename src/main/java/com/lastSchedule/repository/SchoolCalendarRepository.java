package com.lastSchedule.repository;

import com.lastSchedule.entity.GroupCalendar;
import com.lastSchedule.entity.SchoolCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Calendar;
import java.util.List;

public interface SchoolCalendarRepository extends JpaRepository<SchoolCalendar, Long> {
    List<SchoolCalendar> findBySchoolSchoolId(Long schoolId);
}