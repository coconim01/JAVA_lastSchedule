package com.lastSchedule.controller;

import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.GroupFormDto;

import com.lastSchedule.dto.IssueFormDto;
import com.lastSchedule.dto.IssueSearchDto;

import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupMember;
import com.lastSchedule.entity.Issue;

import com.lastSchedule.entity.Member;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.service.GroupService;
import com.lastSchedule.service.IssueService;

import com.lastSchedule.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Component
public class IssueController {


    private GroupService groupService;

    private final MemberService memberService;
    private IssueService issueService;

    public IssueController(GroupService groupService, MemberService memberService, IssueService issueService){
        this.groupService = groupService;
        this.memberService = memberService;
        this.issueService = issueService;
    }

    @GetMapping(value = "/start/new")
    public String newForm(){
        return "new/newForm";
    }



    //이슈 폼으로 가는 메서드
    @GetMapping(value = "/project/new/{groupId}")
    public String issueForm(@PathVariable("groupId") Long groupId, Model model, Principal principal){

        model.addAttribute("issueFormDto", new IssueFormDto());

//        Group group = groupService.getGroupById(groupId);
        model.addAttribute("groupId", groupId);

        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        String loggedInMemberName  = memberService.getMemberNameByEmail(email);
        model.addAttribute("EditorName", loggedInMemberName);

        return "/issue/issueInsert";
    }


    @PostMapping(value = "/project/new/{groupId}")
    public String ProjectNew(@Valid IssueFormDto dto, BindingResult error, @PathVariable("groupId") Long groupId, Model model, Principal principal){
        System.out.println("dto : "+dto);
        System.out.println("groupId : "+groupId);
        if(error.hasErrors()){
            return "/issue/issueInsert";
        }
        // 시작일과 마감일 비교
        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "마감일은 시작일 이후로 설정해주세요.");
            return "/issue/issueInsert";
        }
        // status 필드를 검사하여 null일 경우 "TODO"로 대체
        if (dto.getStatus() == null) {
            dto.setStatus(Status.TODO);
        }
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        String loggedInMemberName  = memberService.getMemberNameByEmail(email);

        dto.setEditor(loggedInMemberName);
        //groupId 설정
        dto.setGroupId(groupId);
        try {
            issueService.saveIssue(dto); //이슈 저장
            //그룹 정보 추가
            Group group = groupService.getGroupById(groupId);
            model.addAttribute("group", group);
        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");
            return "/issue/issueInsert";
        }
        return "redirect:/project/list/group/" + groupId;
    }

    @GetMapping(value = {"/project/list/group/{groupId}", "/project/list/group/{groupId}/{page}"})
    public String projectMange(IssueSearchDto searchDto,  @PathVariable("groupId") Long groupId, @PathVariable(value = "page") Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        searchDto.setGroupId(groupId);
        Page<Issue> issues = issueService.getAdminIssuePage(searchDto, groupId, pageable);
        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Long loggedInMemberId  = memberService.getMemberIdByEmail(email);
        //로그인한 회원의 그룹 조회
        List<Group> groups = groupService.getGroupsByLoggedInMemberId(loggedInMemberId);

        Optional<String> groupName = groups.stream()
                .filter(group -> group.getId().equals(groupId))
                .map(Group::getGroupName).findFirst();


        issues.forEach(issue -> {
            LocalDateTime startDate = issue.getStartDate();
            LocalDateTime finishDate = issue.getFinishDate();
            long daysBetween = ChronoUnit.DAYS.between(startDate, finishDate);
            String periodStr = String.format("%d일", daysBetween);
            issue.setPeriod(periodStr); // Issue 객체에 기간 정보 추가
        });

        model.addAttribute("groupIds", Arrays.asList(groupId));
        model.addAttribute("groupId", groupId);


        model.addAttribute("groupName", groupName.orElse(""));
        model.addAttribute("issues", issues) ;
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5);

        return "issue/issueList" ;
    }

    @GetMapping(value = "/project/detail/{projectId}")
    public String issueDetail(Model model, @PathVariable("projectId") Long id, Long groupId) {
        IssueFormDto dto = issueService.getIssueDetail(id);

        model.addAttribute("project", dto);
        model.addAttribute("groupId", dto.getGroupId());

        return "issue/issueDetail";

    }

    @GetMapping(value = "/project/{projectId}")
    public String doGetUpdate(@PathVariable("projectId") Long id, Model model){
        try{
            IssueFormDto dto = issueService.getIssueDetail(id) ;
            model.addAttribute("dto", dto);

        }catch (EntityNotFoundException err){
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.") ;
            model.addAttribute("dto", new IssueFormDto());
        }
        return "issue/issueUpdate";
    }

    @PostMapping(value = "/project/{projectId}")
    public String issueUpdate(@Valid IssueFormDto dto, BindingResult error, Model model) {
        String whenError = "/issue/issueUpdate";
        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            //model.addAttribute("dto", dto);
            return whenError;
        }
        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "마감일은 시작일 이후로 설정해주세요.");
            return "/issue/issueUpdate";
        }
        try {
            issueService.updateIssue(dto);
        } catch (Exception err) {
            model.addAttribute("errorMessage", "이슈 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }
        return "redirect:/groupProject/list"; // 수정 후 이슈 리스트로 이동
    }

    @GetMapping(value = "/project/delete/{id}")
    public String deleteIssue(@PathVariable("id") Long id, @RequestParam("groupId") Long groupId) {
//        Long groupId = issueFormDto.getId();
        issueService.deleteIssue(id);

        return "redirect:/project/list/group/" + groupId;

    }

    @GetMapping("/groupProject/list")
    public String projectListByGroupId(Model model, Principal principal){
        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Long loggedInMemberId  = memberService.getMemberIdByEmail(email);

        //로그인한 회원의 그룹아이디 조회
        List<Long> groupIds = groupService.getGroupIdsByLoggedInMemberId(loggedInMemberId);
        System.out.println("로그인한 회원의 그룹 아이디 리스트 : " + groupIds);

        List<Group> groups = groupService.getGroupsByLoggedInMemberId(loggedInMemberId);

        //그룹네임을 출력하기 위한
        Map<Long, String> groupNamesMap = new HashMap<>();

        for (Group group : groups) {
            groupNamesMap.put(group.getId(), group.getGroupName());
        }
        model.addAttribute("groupNamesMap", groupNamesMap);

        //그룹아이디-이슈데이터를 출력하기 위한
        Map<Long, List<Issue>> groupIssuesMap = new HashMap<>();
        System.out.println("groupIssuesMap : " + groupIssuesMap);

        for (Group group : groups) {
            Long groupId = group.getId();
            List<Issue> issues = issueService.getIssuesByGroupId(groupId);
            System.out.println("issues : " + issues);

            // 기간 계산 및 Issue 객체에 기간 정보 추가
            issues.forEach(issue -> {
                LocalDateTime startDate = issue.getStartDate();
                LocalDateTime finishDate = issue.getFinishDate();
                long daysBetween = ChronoUnit.DAYS.between(startDate, finishDate);
                String periodStr = String.format("%d일", daysBetween);
                issue.setPeriod(periodStr);
            });

            groupIssuesMap.put(groupId, issues);

            System.out.println("issues.size : " + issues.size());
        }

        System.out.println("groupIssuesMap : " + groupIssuesMap);

        model.addAttribute("groupIssueMap", groupIssuesMap);
        return "/group/groupIssueList";
    }
}