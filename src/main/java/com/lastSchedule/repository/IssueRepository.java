package com.lastSchedule.repository;

import com.lastSchedule.dto.IssueSearchDto;
import com.lastSchedule.entity.Issue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IssueRepository extends JpaRepository<Issue, Long>, IssueRepositoryCustom{

  Page<Issue> getAdminIssuePage(IssueSearchDto searchDto,Long groupId, Pageable pageable);

    Issue findByCalendarId(Long calendarId);

  List<Issue> findByGroupId(Long groupId);
}