package com.lastSchedule.repository;

import com.lastSchedule.dto.IssueSearchDto;
import com.lastSchedule.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IssueRepositoryCustom {

    Page<Issue> getAdminIssuePage(IssueSearchDto searchDto, Long groupId, Pageable pageable);


}
