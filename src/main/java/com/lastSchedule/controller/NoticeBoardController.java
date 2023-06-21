package com.lastSchedule.controller;

import com.lastSchedule.dto.NoticeBoardFormDto;
import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.service.NoticeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
public class NoticeBoardController {
    private final NoticeBoardService noticeBoardService;
    private final MemberRepository memberRepository;

    @GetMapping(value = "/noticeboard/new")
    public String noticeBoardForm(Model model){
        model.addAttribute("noticeBoardFormDto", new NoticeBoardFormDto());

        return "/noticeBoard/noticeBoardInsert";
    }

    private void getGroupNumber() {
    }

    @PostMapping(value = "/noticeboard/new")
    public String noticeBoardNew(@Valid NoticeBoardFormDto dto, BindingResult error, Model model){
        if(error.hasErrors()){
            System.out.println("err1");

            // 오류 정보 출력
            for (ObjectError objError : error.getAllErrors()) {
                System.out.println(objError);
            }

            return "/noticeBoard/noticeBoardInsert";
        }
        if(dto.getNb_startDate().isAfter(dto.getNb_finishDate())){
            System.out.println("err2");

            error.rejectValue("nb_finishDate", "error.nb_finishDate", "종료일은 시작일 이후로 설정해주세요.");
            return "/noticeBoard/noticeBoardInsert";
        }
        try {
            noticeBoardService.saveNoticeBoard(dto);
            System.out.println("nb_groupNumber1 : " + dto.getNb_groupNumber());

        }catch (Exception err){
            err.printStackTrace();
            System.out.println("err3");

            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/noticeBoard/noticeBoardInsert";
        }
        System.out.println("nb_groupNumber2 : " + dto.getNb_groupNumber());
        return "redirect:/noticeboard/list";
    }


    @GetMapping(value = {"/noticeboard/list", "noticeboard/list/{page}"})
    public String noticeBoardManage(NoticeBoardSearchDto searchDto, @PathVariable("page") Optional<Integer> page, Model model, HttpServletRequest httpServletRequest) {
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);

        Page<NoticeBoard> noticeBoards = noticeBoardService.getAdminNoticeBoardPage(searchDto, pageable);

        model.addAttribute("noticeBoards", noticeBoards); // 수정된 부분
        model.addAttribute("searchDto", searchDto);
        model.addAttribute("maxPage", 5);

        return "noticeBoard/noticeBoardList";
    }


    @GetMapping(value = "/noticeboard/detail/{nb_no}")
    public String noticeBoardDetail(HttpServletRequest request, Model model, @PathVariable("nb_no") Long no) {
        int updatedViewCount = noticeBoardService.incrementViewCount(no);

        NoticeBoardFormDto dto = noticeBoardService.getNoticeBoardDetail(no);
        model.addAttribute("noticeBoard", dto);
        model.addAttribute("updatedViewCount", updatedViewCount);

        return "noticeBoard/noticeBoardDetail";
    }

    @GetMapping(value = "/noticeboard/{nb_no}")
    public String deGetUpdate(@PathVariable("nb_no") Long no, Model model) {
        try {
            NoticeBoardFormDto dto = noticeBoardService.getNoticeBoardDetail(no);
            model.addAttribute("dto", dto);
        }catch (EntityNotFoundException err) {
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.");
            model.addAttribute("dto", new NoticeBoardFormDto());
        }
        return "noticeBoard/noticeBoardUpdate";
    }

    @PostMapping(value = "/noticeboard/{nb_no}")
    public String noticeBoardUpdate(@Valid NoticeBoardFormDto dto, BindingResult error, Model model) {
        String whenError = "/noticeBoard/noticeBoardUpdate";
        System.out.println("err4");
        // 오류 정보 출력
        for (ObjectError objError : error.getAllErrors()) {
            System.out.println(objError);
        }

        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            System.out.println("err5");
            model.addAttribute("dto", dto);
            return whenError;
        }

        if (dto.getNb_startDate().isAfter(dto.getNb_finishDate())) {
            error.rejectValue("nb_finishDate", "error.nb_finishDate", "마감일은 시작일 이후로 설정해주세요.");
            return "/noticeBoard/noticeBoardUpdate";
        }

        try {
            noticeBoardService.updateNoticeBoard(dto);
        }catch (Exception err) {
            model.addAttribute("errorMessage", "일정 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/noticeboard/list";
    }

    @GetMapping(value = "noticeboard/delete/{no}")
    public String deleteNoticeBoard(@PathVariable("no") Long no) {
        noticeBoardService.deleteNoticeBoard(no);

        return "redirect:/noticeboard/list";
    }
}
