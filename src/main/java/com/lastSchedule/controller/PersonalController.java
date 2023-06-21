package com.lastSchedule.controller;


import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.dto.PersonalSearchDto;
import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.service.PersonalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class PersonalController {
    @GetMapping(value = "/personal/new")
    public String personalForm(Model model){
        model.addAttribute("personalFormDto", new PersonalFormDto());
        return "/personal/personalInsert";
    }

    private final PersonalService personalService;
    private final MemberRepository memberRepository;

    @PostMapping(value = "/personal/new")
    public String ProjectNew(@RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime startDate, @Valid PersonalFormDto dto, BindingResult error, Model model, Principal principal){
        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);
        if(error.hasErrors()){
            return "/personal/personalInsert";
        }
        // 시작일과 마감일 비교
        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "종료일은 시작일 이후로 설정해주세요.");
            return "/personal/personalInsert";
        }
        try {
            personalService.savePersonal(dto, member);

        }catch (Exception err){
            err.printStackTrace();
            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/personal/personalInsert";
        }
        return "redirect:/personal/list";
    }

    @GetMapping(value = {"/personal/list", "/personal/list/{page}"})
    public String personalMange(PersonalSearchDto searchDto, @PathVariable("page") Optional<Integer> page,
                                Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<Personal> personals = personalService.getAdminPersonalPage(searchDto, pageable) ;

        for(Personal personal : personals){
            LocalDateTime startDate = personal.getStartDate();
            LocalDateTime finishDate = personal.getFinishDate();
            long daysBetween = ChronoUnit.DAYS.between(startDate, finishDate);
            String periodStr = String.format("%d일", daysBetween);
            personal.setPeriod(periodStr);
        }
        model.addAttribute("personals", personals) ;
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 최대 페이지 번호
        return "personal/personalList" ;
    }

    @GetMapping(value = "/personal/detail/{personalId}")
    public String personalDetail(Model model, @PathVariable("personalId") Long id) {
        PersonalFormDto dto = personalService.getPersonalDetail(id);
        model.addAttribute("personal", dto);

        return "personal/personalDetail";

    }
    @GetMapping(value = "/personal/{personalId}")
    public String doGetUpdate(@PathVariable("personalId") Long personalId, Model model){
        try{
            PersonalFormDto dto = personalService.getPersonalDetail(personalId) ;
            model.addAttribute("dto", dto);

        }catch (EntityNotFoundException err){
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.") ;
            model.addAttribute("dto", new PersonalFormDto());
        }
        return "personal/personalUpdate";
    }

    @PostMapping(value = "/personal/{personalId}")
    public String personalUpdate(@Valid PersonalFormDto dto, BindingResult error, Model model) {
        String whenError = "/personal/personalUpdate";
        System.out.println("잘 넘어오나 확인하기 "+ dto.toString());
        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            model.addAttribute("dto", dto);
            return whenError;
        }

        if (dto.getStartDate().isAfter(dto.getFinishDate())) {
            error.rejectValue("finishDate", "error.finishDate", "마감일은 시작일 이후로 설정해주세요.");
            return "/personal/personalUpdate";
        }

        try {
            personalService.updatePersonal(dto);
            System.out.println("업데이티 확인");

        } catch (Exception err) {
            model.addAttribute("errorMessage", "일정 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/personal/list";
    }



    @GetMapping(value = "personal/delete/{id}")
    public String deletePersonal(@PathVariable("id") Long id) {
        personalService.deletePersonal(id);

        return "redirect:/personal/list";
    }
}