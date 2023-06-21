package com.lastSchedule.service;

import com.lastSchedule.dto.IssueFormDto;
import com.lastSchedule.dto.IssueSearchDto;
import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupCalendar;
import com.lastSchedule.entity.Issue;


import com.lastSchedule.entity.PersonalCalendar;
import com.lastSchedule.repository.GroupCalendarRepository;
import com.lastSchedule.repository.GroupRepository;
import com.lastSchedule.repository.IssueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class IssueService {
    private final IssueRepository issueRepository;
    private final GroupRepository groupRepository;
    private final GroupService groupService;
    private final GroupCalendarRepository groupCalendarRepository;

    public Long saveIssue(IssueFormDto dto)throws Exception{

        Issue issue = dto.createIssue();
        GroupCalendar groupCalendar = dto.createGroupCalendar(issue);
        groupCalendar.setGroup(issue.getGroup());
        GroupCalendar savedCalendar = groupCalendarRepository.save((groupCalendar));

        issue.setCalendarId(savedCalendar.getCalendar_id());

        Group group = groupService.getGroupById(dto.getGroupId());
        System.out.println("현재 생성된 그룹 아이디 : " + dto.getGroupId());
        issue.setGroup(group);

        issueRepository.save(issue);
        System.out.println("Issue 저장 완료: " + issue.getId());
        System.out.println("함께 들어갈 캘린더 아이디 : " + issue.getCalendarId());


        return issue.getId().longValue();
    }

    public Page<Issue> getAdminIssuePage(IssueSearchDto searchDto, Long groupId, Pageable pageable) {
        return issueRepository.getAdminIssuePage(searchDto, groupId, pageable);
    }

    public IssueFormDto getIssueDetail(Long id) {
        Issue issue = issueRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        IssueFormDto dto = IssueFormDto.of(issue);

        return dto;
    }

    public Long updateIssue(IssueFormDto dto) throws Exception {
        Issue issue = issueRepository.findById(dto.getId())
                .orElseThrow(EntityNotFoundException::new);


        GroupCalendar groupCalendar = groupCalendarRepository.findById(issue.getCalendarId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("캘린더 확인" + groupCalendar.toString());

        issue.updateIssue(dto);
        groupCalendar.updateGroupCalendar(issue);
        return issue.getId();
    }

    public void deleteIssue(Long id) {
        issueRepository.deleteById(id);
    }

    public List<Issue> getAllIssues() {
        return issueRepository.findAll();
    }

    public List<Issue> getIssuesByGroupId(Long groupId) {
        System.out.println("출력될 그룹 아이디 : " + groupId);
        return issueRepository.findByGroupId(groupId);

    }

    public List<Issue> getIssuesByGroupIds(List<Long> groupIds) {
        List<Issue> allIssues = new ArrayList<>();

        for (Long groupId : groupIds) {
            List<Issue> issues = issueRepository.findByGroupId(groupId);
            allIssues.addAll(issues);
        }

        return allIssues;
    }
}


