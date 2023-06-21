package com.lastSchedule.repository;

import com.lastSchedule.dto.SchoolIssueSearchDto;
import com.lastSchedule.entity.SchoolIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SchoolIssueRepository extends JpaRepository<SchoolIssue, Long>, SchoolIssueRepositoryCustom {
    Page<SchoolIssue> getAdminSchoolIssuePage(SchoolIssueSearchDto searchDto, Pageable pageable);
    SchoolIssue findByCalendarId(Long calendarId);

    List<SchoolIssue> findBySchoolCode(String schoolCode);
}
