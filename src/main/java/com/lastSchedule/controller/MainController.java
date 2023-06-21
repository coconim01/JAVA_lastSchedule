package com.lastSchedule.controller;


import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.dto.SchoolTimeBoardDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.*;
import com.lastSchedule.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.expression.SecurityExpressionOperations;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final NoticeBoardService noticeBoardService;
    private final FreeBoardService freeBoardService;
    private final SuggestBoardService suggestBoardService;
    private final PersonalService personalService;
    private final MemberRepository memberRepository;
    private final SchoolTimeBoardRepository schoolTimeBoardRepository;
    private final SchoolTimeBoardService schoolTimeBoardService;

    public MainController(NoticeBoardService noticeBoardService, FreeBoardService freeBoardService, SuggestBoardService suggestBoardService, PersonalService personalService, MemberRepository memberRepository, SchoolTimeBoardRepository schoolTimeBoardRepository, SchoolTimeBoardService schoolTimeBoardService) {
        this.noticeBoardService = noticeBoardService;
        this.freeBoardService = freeBoardService;
        this.suggestBoardService = suggestBoardService;
        this.personalService = personalService;
        this.memberRepository = memberRepository;
        this.schoolTimeBoardRepository = schoolTimeBoardRepository;
        this.schoolTimeBoardService = schoolTimeBoardService;
    }

    @GetMapping("")
    public String showBoardList(Model model, Authentication authentication, HttpServletRequest httpServletRequest) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/members/login";
        }

        String schoolCode = memberRepository.findByEmail(httpServletRequest.getRemoteUser()).getSchoolCode();

        List<NoticeBoard> noticeBoards = noticeBoardService.getAllNoticeBoards().stream()
                .filter(board -> board.getSchoolCode().equals(schoolCode))
                .collect(Collectors.toList());

        List<FreeBoard> freeBoards = freeBoardService.getAllFreeBoards();
        List<SuggestBoard> suggestBoards = suggestBoardService.getAllSuggestBoards();

        // id가 높은 순서로 가져온 게시글을 처리하기 위해 리스트를 뒤집습니다.
        Collections.reverse(noticeBoards);
        Collections.reverse(freeBoards);
        Collections.reverse(suggestBoards);

        List<NoticeBoard> latestNoticeBoards = getLatestItems(noticeBoards, 5);
        List<FreeBoard> latestFreeBoards = getLatestItems(freeBoards, 5);
        List<SuggestBoard> latestSuggestBoards = getLatestItems(suggestBoards, 5);

        model.addAttribute("noticeBoards", latestNoticeBoards);
        model.addAttribute("freeBoards", latestFreeBoards);
        model.addAttribute("suggestBoards", latestSuggestBoards);

        return "main";
    }

    private <T> List<T> getLatestItems(List<T> items, int count) {
        int size = Math.min(items.size(), count);
        return items.subList(0, size);
    }

    @GetMapping("/personal/main")
    public String showPersonalMain(Model model, Authentication authentication, Principal principal) {
        Member member = memberRepository.findByEmail(principal.getName());
        SchoolTimeBoardDto dto = new SchoolTimeBoardDto();

        //실제 있는 시간표로 불러오기
        SchoolTimeBoard schoolTimeBoard = schoolTimeBoardRepository.findByMemberId(member.getId());
        System.out.println("시간표 불러오기"+schoolTimeBoard);

        if(schoolTimeBoard == null){
            //만약 시간표가 없다면 기본 객체 생성 ( 모든 시간표의 값을 "=="으로" )
            schoolTimeBoard = schoolTimeBoardService.createTimeBoard(member);
        }

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/members/login";
        }
        List<Personal> personals = personalService.getAllPersonalList();


        List<Personal> latestPersonals = getLatestItems(personals, 5);
        SchoolTimeBoardDto schoolTimeBoardDto = dto.of(schoolTimeBoard);
        System.out.println("schoolTimeBoardDto : "+ schoolTimeBoardDto);


        model.addAttribute("latestPersonals", latestPersonals);
        model.addAttribute("schoolTimeBoard", schoolTimeBoard);
        model.addAttribute("schoolTimeBoardDto", schoolTimeBoardDto);



        System.out.println("showPersonalMain");
        return "personal/mypersonalMain";
    }

    @PostMapping(value = "/personal/main")
    public String timeBoard(@ModelAttribute SchoolTimeBoardDto schoolTimeBoardDto, BindingResult error, Model model, Principal principal){
        model.addAttribute("schoolTimeBoardDto", schoolTimeBoardDto);
        System.out.println("schoolTimeBoardDtoId : "+ schoolTimeBoardDto.getTimeBoardId());
        System.out.println("schoolTimeBoardDto : "+ schoolTimeBoardDto);


        if(error.hasErrors()){
            return "/personal/mypersonalMain";
        }
        try {
            schoolTimeBoardService.updateTimeBoard(schoolTimeBoardDto);
        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/personal/mypersonalMain";
        }
        return "redirect:main";
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


        @GetMapping("/events/personal")
        public String gotoPage() {
            return "personalcalendar";
        }

        @GetMapping("/events/personal/calendar") //ajax 데이터 전송 URL
        public @ResponseBody List<Map<String, Object>> getEvent(Principal principal) throws IOException {
            String email = principal.getName();
            Member member = memberRepository.findByEmail(email);
            Long id = member.getId();
            //id = 1L;

            List<GroupMember>groupList = groupMemberRepository.findByMemberId(id);
            //groups_id 확인용

            //member에 있는 schoolcode를 사용하여 school 객체 찾아서 해당 schoolId 가져오기
            Long schoolId = schoolRepository.findBySchoolCode(member.getSchoolCode()).getSchoolId() ;


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

        @GetMapping(value = "/personal_event/detail/{calendarId}")
        public String personalCalendarDetail(Model model, @PathVariable("calendarId") Long calendarId) {
            Personal person = personalRepository.findByCalendarId(calendarId);
            System.out.println("calendarId : "+ calendarId);
            System.out.println("person : "+ person.getPersonalId());

            PersonalFormDto dto = personalService.getPersonalDetail(person.getPersonalId());
            model.addAttribute("personal", dto);

            return "personal/personalDetail";
        }
    }



