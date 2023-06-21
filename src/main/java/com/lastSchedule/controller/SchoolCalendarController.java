package com.lastSchedule.controller;

import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.dto.SchoolIssueFormDto;
import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.entity.School;
import com.lastSchedule.entity.SchoolIssue;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.repository.SchoolIssueRepository;
import com.lastSchedule.repository.SchoolIssueRepositoryCustom;
import com.lastSchedule.repository.SchoolRepository;
import com.lastSchedule.service.GroupCalendarService;
import com.lastSchedule.service.SchoolCalendarService;
import com.lastSchedule.service.SchoolIssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("")
public class SchoolCalendarController {
    @Autowired
    private SchoolCalendarService eventService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolIssueRepository schoolIssueRepository;
    @Autowired
    private SchoolIssueService schoolIssueService;

    @GetMapping("/events/school")
    public String gotoPage() {
        return "schoolcalendar";
    }

    @GetMapping("/events/school/calendar") //ajax 데이터 전송 URL
    public @ResponseBody List<Map<String, Object>> getEvent(Model model, Principal principal) throws IOException {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        String schoolcode = member.getSchoolCode();
        School school = schoolRepository.findBySchoolCode(schoolcode);
        System.out.println("school : "+school);

        Long schoolId = school.getSchoolId();

        return eventService.getEventList(schoolId);
    }

    @GetMapping(value = "/schoolIssue_event/detail/{calendarId}")
    public String schoolIssueCalendarDetail(Model model, @PathVariable("calendarId") Long calendarId) {
        SchoolIssue schoolIssue = schoolIssueRepository.findByCalendarId(calendarId);
        System.out.println("calendarId : "+ calendarId);
        System.out.println("schoolIssue : "+ schoolIssue.getSchoolIssueId());

        SchoolIssueFormDto dto = schoolIssueService.getSchoolIsseuDetail(schoolIssue.getSchoolIssueId());
        model.addAttribute("schoolIssue", dto);

        return "schoolIssue/schoolIssueDetail";
    }


}
