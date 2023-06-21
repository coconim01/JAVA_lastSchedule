package com.lastSchedule.controller;

import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.dto.IssueFormDto;
import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.*;
import com.lastSchedule.service.GroupCalendarService;
import com.lastSchedule.service.IssueService;
import com.lastSchedule.service.SchoolCalendarService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("")
public class GroupCalendarController {
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private SchoolCalendarService exRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private GroupMemberRepository groupmemberRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolCalendarService schoolCalendarService;
    @Autowired
    private GroupCalendarService groupCalendarService;
    @Autowired
    private IssueRepository issueRepository;
    @Autowired
    private IssueService issueService;

    @GetMapping(value = "/group_event/detail/{calendarId}")
    public String personalCalendarDetail(Model model, @PathVariable("calendarId") Long calendarId) {
        Issue issue = issueRepository.findByCalendarId(calendarId);
        System.out.println("calendarId : " + calendarId);
        System.out.println("issue : " + issue.getId());

        IssueFormDto dto = issueService.getIssueDetail(issue.getId());
        model.addAttribute("project", dto);
        model.addAttribute("groupId", dto.getId());

        return "Issue/IssueDetail";
    }

    //로그인 사람 기준! 그룹 캘린더 확인하기
    @GetMapping("/events/group")
    public String gotoPage2(Model model) {

        return "groupcalendar";
    }


    @GetMapping("/events/group/calendar") //ajax 데이터 전송 URL
    public @ResponseBody List<Map<String, Object>> gotGroup( Principal principal) throws IOException {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        Long id = member.getId();
        String schoolcode = member.getSchoolCode();

        School school = schoolRepository.findBySchoolCode(schoolcode);
        System.out.println("school : " + school);

        Long schoolId = school.getSchoolId();

        //schoolId 해당 학교에 있는 calendar 가져오고 eventList에 담기
        List<Map<String, Object>> eventList = new ArrayList<>();

        List<Map<String, Object>> SchoolCalendarList = schoolCalendarService.getEventList(schoolId);
        eventList.addAll(SchoolCalendarList);


        List<GroupMember> groupmemberList = groupmemberRepository.findByMemberId(id);

        //해당 groupId에 대한 groupcalendar 가져와서 eventList에 담기
        for (GroupMember groupMember : groupmemberList) {
            System.out.println("아이디가 속해 있는 그룹아이디 : " + groupMember.getGroup().getId());
        }

        System.out.println("////////아이디가 속한 그룹 캘린더 //////////");

        for (GroupMember item : groupmemberList) {
            eventList.addAll(groupCalendarService.getEventList(item.getGroup()));
        }
        return eventList;
    }
}