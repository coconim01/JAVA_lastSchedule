package com.lastSchedule.repository;

import com.lastSchedule.dto.SchoolIssueSearchDto;
import com.lastSchedule.entity.SchoolIssue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SchoolIssueRepositoryCustom {
    Page<SchoolIssue> getAdminSchoolIssuePage(SchoolIssueSearchDto searchDto, Pageable pageable);

}
