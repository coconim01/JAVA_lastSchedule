package com.lastSchedule.controller;

import com.lastSchedule.dto.FreeBoardFormDto;
import com.lastSchedule.dto.FreeBoardSearchDto;
import com.lastSchedule.entity.FreeBoard;
import com.lastSchedule.service.FreeBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping(value = "/freeboard")
public class FreeBoardController {
    private final FreeBoardService freeBoardService;

    @GetMapping(value = "/new")
    public String freeBoardForm(Model model){
        model.addAttribute("freeBoardFormDto", new FreeBoardFormDto());

        return "/FreeBoard/FreeBoardInsert";
    }

    private void getGroupNumber() {
    }

    @PostMapping(value = "/new")
    public String freeBoardNew(@Valid FreeBoardFormDto dto, BindingResult error, Model model){
        if(error.hasErrors()){
            System.out.println("err1");

            // 오류 정보 출력
            for (ObjectError objError : error.getAllErrors()) {
                System.out.println(objError);
            }

            return "/FreeBoard/FreeBoardInsert";
        }

        try {
            freeBoardService.saveFreeBoard(dto);
            System.out.println("Fb_groupNumber1 : " + dto.getFb_groupNumber());

        }catch (Exception err){
            err.printStackTrace();
            System.out.println("err3");

            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/FreeBoard/FreeBoardInsert";
        }
        System.out.println("Fb_groupNumber2 : " + dto.getFb_groupNumber());
        return "redirect:/freeboard/list";
    }

    @GetMapping(value = {"/list", "/list/{page}"})
    public String freeBoardMange(FreeBoardSearchDto searchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<FreeBoard> freeBoards = freeBoardService.getAdminFreeBoardPage(searchDto, pageable);

        model.addAttribute("freeBoards", freeBoards);
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 최대 페이지 번호

        return "/freeBoard/freeBoardList";
    }

    @GetMapping(value = "/detail/{fb_no}")
    public String freeBoardDetail(HttpServletRequest request, Model model, @PathVariable("fb_no") Long no) {
        int updatedViewCount = freeBoardService.incrementViewCount(no);

        FreeBoardFormDto dto = freeBoardService.getFreeBoardDetail(no);
        model.addAttribute("freeBoard", dto);
        model.addAttribute("updatedViewCount", updatedViewCount);
        return "/freeBoard/freeBoardDetail";
    }

    @GetMapping(value = "/{fb_no}") // 수정하기
    public String doGetUpdate(@PathVariable("fb_no") Long no, Model model) {
        try {
            FreeBoardFormDto dto = freeBoardService.getFreeBoardDetail(no);
            model.addAttribute("dto", dto);
        }catch (EntityNotFoundException err) {
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.");
            model.addAttribute("dto", new FreeBoardFormDto());
        }

        return "freeBoard/freeBoardUpdate";
    }

    @PostMapping(value = "/{fb_no}")
    public String doPostUpdate(@Valid FreeBoardFormDto dto,  BindingResult error, Model model) {
        String whenError = "/freeBoard/freeBoardUpdate";
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

        try {
            freeBoardService.updateFreeBoard(dto);
        }catch (Exception err) {
            model.addAttribute("errorMessage", "일정 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }

        return "redirect:/freeboard/list";
    }

    @GetMapping(value = "/delete/{no}")
    public String deleteFreeBoard(@PathVariable("no") Long no) { // 삭제하기
        freeBoardService.deleteFreeBoard(no);

        return "redirect:/freeboard/list";
    }
}
