package com.lastSchedule.controller;

import com.lastSchedule.dto.GroupFormDto;
import com.lastSchedule.dto.IssueFormDto;
import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupCalendar;
import com.lastSchedule.entity.GroupMember;
import com.lastSchedule.entity.Member;
import com.lastSchedule.repository.GroupCalendarRepository;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.service.GroupService;
import com.lastSchedule.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@Component
@RequestMapping("/group")
public class GroupController {

    private final GroupService groupService;
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final GroupCalendarRepository groupCalendarRepository;

    public GroupController(MemberService memberService, GroupService groupService, MemberRepository memberRepository, GroupCalendarRepository groupCalendarRepository){
        this.memberService = memberService;
        this.groupService = groupService;
        this.memberRepository = memberRepository;
        this.groupCalendarRepository = groupCalendarRepository;
    }

    //그룹 폼으로 가는 메서드
    @GetMapping(value = "/new")
    public String groupForm(Model model, Principal principal){

        GroupFormDto groupFormDto = new GroupFormDto(); // 그룹 폼 데이터 초기화
        String loggedInUserEmail = principal.getName();
        Member loggedInMember = memberService.findByEmail(loggedInUserEmail);
        String loggedInMemberName = memberService.getMemberNameByEmail(loggedInUserEmail);
        model.addAttribute("EditorName", loggedInMemberName);
//        List<Member> members = memberService.getAllMembers(); //전체 회원 조회
        String schoolCode = null;

        schoolCode = loggedInMember.getSchoolCode();

        List<Member> matchedMembers = null;
        if (schoolCode != null) {
            matchedMembers = memberService.findBySchoolCode(schoolCode);
            matchedMembers = matchedMembers.stream()
                    .filter(member -> !member.getId().equals(loggedInMember.getId()))
                    .collect(Collectors.toList());;
        }

        model.addAttribute("members", matchedMembers);
        model.addAttribute("groupFormDto", groupFormDto);
        return "/group/groupInsert";
    }


    //그룹 폼 데이터 저장
    @PostMapping(value = "/new")
    public String groupNew(@ModelAttribute("groupFormDto") GroupFormDto groupFormDto, BindingResult error, @RequestParam("action") String action, Model model, Principal principal){

        if(error.hasErrors()){
            return "/group/groupInsert";
        }
        try {
            if(action.equals("group")) {
                String email = principal.getName();
                Member loggedInMember = memberService.findByEmail(email);

                //작성자를 그룹 멤버로 추가
                groupFormDto.getMemberIdList().add(loggedInMember.getId());

                int totalMemberCount = groupFormDto.getMemberIdList().size();
                if (totalMemberCount == 1) {
                    totalMemberCount = 2;
                }
                groupFormDto.setHeadCount(totalMemberCount);

                // 그룹 생성 및 그룹 ID 가져오기
                Long groupId = groupService.Group(groupFormDto, principal);
//            Group group = groupService.getGroupById(groupId);
//              groupFormDto.setId(groupId); // 그룹 아이디 설정

                return "redirect:/group/mylist";

            }else if(action.equals("issue")) {
                String email = principal.getName();
                Member loggedInMember = memberService.findByEmail(email);

                //작성자를 그룹 멤버로 추가
                groupFormDto.getMemberIdList().add(loggedInMember.getId());


                //그룹 생성 및 그룹 ID 가져오기
                Long groupId = groupService.Group(groupFormDto, principal);

                int totalMemberCount = groupFormDto.getExistingMemberIds().size() + 1 ;

                if (totalMemberCount == 2) {
                    totalMemberCount = 2;
                }
                groupFormDto.setHeadCount(totalMemberCount);


                return "redirect:/project/new/" + groupId;
            }

        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");
        }

        return "/group/groupInsert";
    }

    @GetMapping(value = {"/list"})
    public String groupManage(GroupFormDto groupFormDto, Model model, Principal principal){
        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Long loggedInMemberId  = memberService.getMemberIdByEmail(email);
        //로그인한 회원의 그룹 조회
        List<Group> groups = groupService.getGroupsByLoggedInMemberId(loggedInMemberId);


        for (Group group : groups) {
            if(StringUtils.isEmpty(group.getSpecialNote())){
                group.setSpecialNote("무");
            }else{
                group.setSpecialNote("유");
            }
        }


        model.addAttribute("groups", groups);

        return "group/groupList" ;
    }

    @GetMapping("/detail/{groupId}")
    public String groupDetail(Model model, @PathVariable("groupId") Long groupId, Principal principal){
        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Long loggedInMemberId  = memberService.getMemberIdByEmail(email);
        List<Member> groupMembers = groupService.getMembersInSameGroupAsLoggedInMember(loggedInMemberId, groupId);

        GroupFormDto dto = groupService.getGroupDetail(groupId);

        model.addAttribute("groupMembers", groupMembers);
        model.addAttribute("group", dto);

        return  "group/groupDetail";

    }

    @GetMapping(value = "/{groupId}")
    public String doGetUpdate(@PathVariable("groupId") Long groupId, Model model, Principal principal){

        try{
            String email = principal.getName();
            //로그인한 회원의 멤버 아이디 조회
            Long loggedInMemberId  = memberService.getMemberIdByEmail(email);

            List<Member> groupMembers = groupService.getMembersInSameGroupAsLoggedInMember(loggedInMemberId, groupId);
            model.addAttribute("groupMembers", groupMembers);
            System.out.println("원하는 그룹 아이디(" + groupId + ")의 그룹 멤버들: " + groupMembers);

            Member loggedInMember = memberService.findByEmail(email);
            String schoolCode = null;
            schoolCode = loggedInMember.getSchoolCode();

            List<Member> otherMembers = groupService.getRemainingMembers(loggedInMemberId, groupId, schoolCode);
            model.addAttribute("otherMembers", otherMembers);
            System.out.println("그룹 멤버 제외 나머지 멤버 : " + otherMembers);

            GroupFormDto dto = groupService.getGroupDetail(groupId) ;
            model.addAttribute("dto", dto);

        }catch (EntityNotFoundException err){
            model.addAttribute("errorMessage", "존재하지 않는 그룹입니다.") ;
            model.addAttribute("dto", new GroupFormDto());
        }
        return "group/groupUpdate";
    }


    @PostMapping(value = "/{groupId}")
    public String groupUpdate(@Valid GroupFormDto dto, BindingResult error, Model model) {

        String whenError = "/group/groupUpdate";

        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            //model.addAttribute("dto", dto);
            return whenError;
        }
        try {
            groupService.updateGroupAndGroupMembers(dto);
        } catch (Exception err) {
            model.addAttribute("errorMessage", "이슈 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }
        return "redirect:/group/mylist";
    }

    @GetMapping(value = "/delete/{id}")
    public String deleteGroup(@PathVariable("id") Long id){

        groupService.deleteGroup(id);

        return "redirect:/group/mylist";
    }

    @GetMapping(value = {"/mylist"})
    public String MygroupManage(GroupFormDto groupFormDto, Model model, Principal principal){
        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Member member = memberRepository.findByEmail(email);
        //로그인한 회원의 그룹 조회
        List<Group> groups = groupService.getMyGroupsByMember(member);


        for (Group group : groups) {
            if(StringUtils.isEmpty(group.getSpecialNote())){
                group.setSpecialNote("무");
            }else{
                group.setSpecialNote("유");
            }
        }

        model.addAttribute("groups", groups);

        return "group/groupList" ;
    }

}