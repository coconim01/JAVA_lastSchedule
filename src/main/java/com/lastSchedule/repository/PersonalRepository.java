package com.lastSchedule.repository;

import com.lastSchedule.dto.PersonalSearchDto;
import com.lastSchedule.entity.Personal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PersonalRepository extends JpaRepository<Personal, Long>, PersonalRepositoryCustom{
    Page<Personal> getAdminPersonalPage(PersonalSearchDto searchDto, Pageable pageable);

    Personal findByCalendarId(Long calendarId);
}
