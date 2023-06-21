package com.lastSchedule.controller;

import com.lastSchedule.dto.MemberFormDto;
import com.lastSchedule.dto.MemberSearchDto;
import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.Member;
import com.lastSchedule.service.GroupService;
import com.lastSchedule.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {
    @GetMapping("/new")
    public String memberForm(Model model){
        // 타임 리프에서 사용할 객체 memberFormDto를 바인딩합니다.
        model.addAttribute("memberFormDto", new MemberFormDto()) ;
        return "/member/memberInsertForm" ;
    }

    private final MemberService memberService ;
    private final PasswordEncoder passwordEncoder;
    private final GroupService groupService;

    @PostMapping("/new")
    public String newMember(@Valid MemberFormDto memberFormDto, BindingResult bindingResult, Model model){
        if(bindingResult.hasErrors()){
            return "/member/memberInsertForm" ;
        }
        try {
            Member member = Member.createMember(memberFormDto, passwordEncoder);
            memberService.saveMember(member);
        }catch (IllegalStateException e){
            model.addAttribute("errMessage", e.getMessage()) ;
            return "/member/memberInsertForm" ;
        }

        System.out.println("포스트 방식 요청 들어옴");
        return "/member/memberLoginForm" ;
    }

    // form 태그와 SecurityConfig.java 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login")
    public String loginMember(){
        return "/member/memberLoginForm" ;
    }

    // SecurityConfig.java 파일에 정의 되어 있습니다.
    @GetMapping(value = "/login/error")
    public String loginError(Model model){
        model.addAttribute("loginErrorMsg", "이메일 또는 비밀 번호를 확인해 주세요.");
        return "/member/memberLoginForm" ;
    }

    @GetMapping(value = {"/list", "/list/{page}"})
    public String memberMange(MemberSearchDto searchDto, @PathVariable("page") Optional<Integer> page, Model model, Principal principal){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Member> members = memberService.getAdminMemberPage(searchDto, pageable);

        //현재 로그인한 사람의 이름=아이디
        String email = principal.getName();
        //로그인한 회원의 멤버 아이디 조회
        Long loggedInMemberId  = memberService.getMemberIdByEmail(email);
        //로그인한 회원의 그룹 조회
        List<Group> groups = groupService.getGroupsByLoggedInMemberId(loggedInMemberId);


        model.addAttribute("groups", groups);
        model.addAttribute("members", members);
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 최대 페이지 번호
        return "/member/memberListForm";
    }
}
