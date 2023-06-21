package com.lastSchedule.controller;

import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.dto.SchoolIssueFormDto;
import com.lastSchedule.dto.SchoolIssueSearchDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.*;
import com.lastSchedule.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class SchoolIssueController {
    @GetMapping(value = "/schoolIssue/new")
    public String schoolIssueForm(Model model){
        model.addAttribute("schoolIssueFormDto", new SchoolIssueFormDto());
        return "/schoolIssue/schoolIssueInsert";
    }

    private final SchoolIssueService schoolIssueService;
    private final MemberRepository memberRepository;
    private final IssueService issueService;
    private final PersonalService personalService;


    @PostMapping(value = "/schoolIssue/new")
    public String ProjectNew(@Valid SchoolIssueFormDto dto, BindingResult error, Model model, Principal principal){
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        if(error.hasErrors()){
            return "/schoolIssue/schoolIssueInsert";
        }
        // 시작일과 마감일 비교
        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "종료일은 시작일 이후로 설정해주세요.");
            return "/schoolIssue/schoolIssueInsert";
        }
        try {
            schoolIssueService.saveSchoolIssue(dto, member);

        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/schoolIssue/schoolIssueInsert";
        }
        return "redirect:/schoolIssue/list";
    }

    @GetMapping(value = {"/schoolIssue/list", "/schoolIssue/list/{page}"})
    public String schoolIssueMange(SchoolIssueSearchDto searchDto, @PathVariable("page") Optional<Integer> page,
                                Model model,HttpServletRequest httpServletRequest){
        String schoolCode = memberRepository.findByEmail(httpServletRequest.getRemoteUser()).getSchoolCode();

        List<SchoolIssue> schoolIssues = schoolIssueService.getIssuesBySchoolCode(schoolCode).stream()
                .sorted(Comparator.comparing(SchoolIssue::getCreatedAt).reversed()) // 생성일자를 기준으로 내림차순 정렬
                .collect(Collectors.toList());

        int pageNumber = page.orElse(1);
        int pageSize = 5;
        int startIndex = (pageNumber - 1) * pageSize;
        int endIndex = Math.min(startIndex + pageSize, schoolIssues.size());

        List<SchoolIssue> pagedSchoolIssues = schoolIssues.subList(startIndex, endIndex);

        schoolIssues.forEach(issue -> {
            LocalDateTime startDate = issue.getStartDate();
            LocalDateTime finishDate = issue.getFinishDate();
            long daysBetween = ChronoUnit.DAYS.between(startDate, finishDate);
            String periodStr = String.format("%d일", daysBetween);
            issue.setPeriod(periodStr);
        });

        model.addAttribute("pagedSchoolIssues", pagedSchoolIssues) ;
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", (int) Math.ceil((double) schoolIssues.size() / pageSize));

        System.out.println("pagedSchoolIssues: " + pagedSchoolIssues);

        model.addAttribute("schoolIssues", pagedSchoolIssues); // 수정된 부분
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 최대 페이지 번호
        model.addAttribute("schoolCode", schoolCode);

        return "schoolIssue/schoolIssueList" ;
    }

    @GetMapping(value = "/schoolIssue/detail/{schoolIssueId}")
    public String schoolIssueDetail(Model model, @PathVariable("schoolIssueId") Long id) {
        SchoolIssueFormDto dto = schoolIssueService.getSchoolIsseuDetail(id);
        model.addAttribute("schoolIssue", dto);

        return "schoolIssue/schoolIssueDetail";

    }
    @GetMapping(value = "/schoolIssue/{schoolIssueId}")
    public String doGetUpdate(@PathVariable("schoolIssueId") Long schoolIssueId, Model model){
        try{
            SchoolIssueFormDto dto = schoolIssueService.getSchoolIsseuDetail(schoolIssueId) ;
            model.addAttribute("dto", dto);

        }catch (EntityNotFoundException err){
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.") ;
            model.addAttribute("dto", new SchoolIssueFormDto());
        }
        return "schoolIssue/schoolIssueUpdate";
    }

    @PostMapping(value = "/schoolIssue/{schoolIssueId}")
    public String schoolIssueUpdate(@Valid SchoolIssueFormDto dto, BindingResult error, Model model) {
        String whenError = "/schoolIssue/schoolIssueUpdate";
        System.out.println("잘 넘어오나 확인하기 "+ dto.toString());
        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            model.addAttribute("dto", dto);
            return whenError;
        }

        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "마감일은 시작일 이후로 설정해주세요.");
            return "/schoolIssue/schoolIssueUpdate";
        }

        try {
            schoolIssueService.updateSchoolIssue(dto);
            System.out.println("업데이티 확인");

        } catch (Exception err) {
            model.addAttribute("errorMessage", "일정 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/schoolIssue/list";
    }

    @GetMapping(value = "schoolIssue/delete/{id}")
    public String deletePersonal(@PathVariable("id") Long id) {
        schoolIssueService.deletesShoolIssue(id);

        return "redirect:/schoolIssue/list";
    }

    private final GroupService groupService;
    private final MemberService memberService;

    @GetMapping("/schoolIssue/main")
    public String showSchoolIssueMain(Model model, Authentication authentication, Principal principal, HttpServletRequest httpServletRequest, @RequestParam(value = "sort", defaultValue = "finishDate") String sortOption) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/members/login";
        }
        String email = principal.getName();
        Long loggedInMemberId = memberService.getMemberIdByEmail(email);
        List<Long> groupIds = groupService.getGroupIdsByLoggedInMemberId(loggedInMemberId);

        List<Issue> issues = issueService.getIssuesByGroupIds(groupIds);

        String schoolCode = memberRepository.findByEmail(httpServletRequest.getRemoteUser()).getSchoolCode();

        List<SchoolIssue> schoolIssues = schoolIssueService.getIssuesBySchoolCode(schoolCode);

        schoolIssues.forEach(issue -> { //스쿨 이슈 남은 기간
            LocalDateTime finishDate = issue.getFinishDate();
            LocalDateTime now = LocalDateTime.now();
            long daysRemaining = ChronoUnit.DAYS.between(now.toLocalDate(), finishDate.toLocalDate());
            String remainingPeriodStr;

            if (daysRemaining > 0) {
                remainingPeriodStr = String.format("%d일 남음", daysRemaining);
            } else if (daysRemaining == 0) {
                remainingPeriodStr = "오늘 마감";
            } else {
                remainingPeriodStr = "마감됨";
            }

            issue.setRemainingPeriod(remainingPeriodStr);
        });

        issues.forEach(issue -> { //그룹이슈 남은 기간
            LocalDateTime finishDate = issue.getFinishDate();
            LocalDateTime now = LocalDateTime.now();
            long daysRemaining = ChronoUnit.DAYS.between(now.toLocalDate(), finishDate.toLocalDate());
            String remainingPeriodStr;

            if (daysRemaining > 0) {
                remainingPeriodStr = String.format("%d일 남음", daysRemaining);
            } else if (daysRemaining == 0) {
                remainingPeriodStr = "오늘 마감";
            } else {
                remainingPeriodStr = "마감됨";
            }

            issue.setRemainingPeriod(remainingPeriodStr);
        });

        List<SchoolIssue> filteredSchoolIssues; // 스쿨이슈 필터링
        if ("finishDate".equals(sortOption)) {
            // 마감일 순으로 정렬
            filteredSchoolIssues = schoolIssues.stream()
                    .sorted(Comparator.comparing(SchoolIssue::getFinishDate))
                    .collect(Collectors.toList());
        } else {
            // 등록일 순으로 정렬 (기본값)
            filteredSchoolIssues = schoolIssues;
            Collections.reverse(filteredSchoolIssues);
        }

        List<Issue> filteredIssues; //그룹 이슈 필터링
        if ("finishDate".equals(sortOption)) {
            // 마감일 순으로 정렬
            filteredIssues = issues.stream()
                    .sorted(Comparator.comparing(Issue::getFinishDate))
                    .collect(Collectors.toList());
        } else {
            // 등록일 순으로 정렬 (기본값)
            filteredIssues = issues;
            Collections.reverse(filteredIssues);
        }

        List<SchoolIssue> latestSchoolIssues = getLatestItems(filteredSchoolIssues, 6);
        List<Issue> latestIssues = getLatestItems(filteredIssues, 6);

        model.addAttribute("latestSchoolIssues", latestSchoolIssues);
        model.addAttribute("latestIssues", latestIssues);
        model.addAttribute("schoolCode", schoolCode);
        model.addAttribute("sortOption", sortOption);

        return "/schoolIssue/schoolIssueMain";
    }

    private <T> List<T> getLatestItems(List<T> items, int count){
        int size = Math.min(items.size(), count);
        return items.subList(0, size);
    }

    @Autowired
    private GroupCalendarService groupCalendarService;
    @Autowired
    private PersonalCalendarService personalCalendarService;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private GroupCalendarRepository exRepository;
    @Autowired
    private PersonalCalendarRepository personalCalendarRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private SchoolCalendarService schoolCalendarService;
    @Autowired
    private  PersonalRepository personalRepository;


    @GetMapping("/events/schoolIssue")
    public String gotoPage() {
        return "personalcalendar";
    }

    @GetMapping("/events/schoolIssue/calendar") //ajax 데이터 전송 URL
    public @ResponseBody List<Map<String, Object>> getEvent(Principal principal) throws IOException {
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        Long id = member.getId();
        //id = 1L;

        List<GroupMember>groupList = groupMemberRepository.findByMemberId(id);
        //groups_id 확인용

        //member에 있는 schoolcode를 사용하여 school 객체 찾아서 해당 schoolId 가져오기
        Long schoolId = schoolRepository.findBySchoolCode(member.getSchoolCode()).getSchoolId() ;

        System.out.println("schoolId : "+ schoolId);

        //schoolId 해당 학교에 있는 calendar 가져오고 eventList에 담기
        List<Map<String, Object>> eventList = new ArrayList<>();

        List<Map<String, Object>> SchoolCalendarList = schoolCalendarService.getEventList(schoolId);
        eventList.addAll(SchoolCalendarList);

        for(GroupMember groupMember : groupList){
            System.out.println("아이디가 속해 있는 그룹아이디 : "+ groupMember.getGroup().getId());
        }

        System.out.println("////////아이디가 속한 그룹 캘린더 //////////");

        for(GroupMember item : groupList){
            eventList.addAll(groupCalendarService.getEventList(item.getGroup()));
        }

        List<PersonalCalendar> schedules = personalCalendarService.schedulesfindAll(id);

        System.out.println("////////////개인 캘린더 /////////////");

        for (PersonalCalendar schedule : schedules) {
            Map<String, Object> event = new HashMap<>();
            System.out.println("id : "+ schedule.getCalendar_id());
            event.put("id", schedule.getCalendar_id());

            System.out.println("url : "+ "/personal_event/detail/"+schedule.getCalendar_id());
            event.put("url", "/personal_event/detail/"+schedule.getCalendar_id());
            System.out.println("title : "+ schedule.getTitle());

            event.put("title", schedule.getTitle());
            System.out.println("start : "+ schedule.getStartTime());

            event.put("start", schedule.getStartTime());
            System.out.println("end : "+ schedule.getEndTime());

            event.put("end", schedule.getEndTime());
            System.out.println("color : "+ schedule.getEventColor()
            );
            event.put("color", schedule.getEventColor());

            System.out.println("description : "+ schedule.getDescription()
            );
            event.put("description", schedule.getDescription());

            System.out.println("importance : "+ schedule.getImportance()
            );
            event.put("importance", schedule.getImportance());

            System.out.println("calendarType : "+ CalendarType.personal
            );
            event.put("calendarType", CalendarType.personal);
            eventList.add(event);
        }
        return eventList;
    }

}