package com.lastSchedule.repository;

import com.lastSchedule.entity.PersonalCalendar;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface PersonalCalendarRepository extends JpaRepository<PersonalCalendar, Long> {
    List<PersonalCalendar> findByMemberId(Long memberId);

}
