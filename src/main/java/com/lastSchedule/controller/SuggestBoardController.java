package com.lastSchedule.controller;

import com.lastSchedule.dto.SuggestBoardFormDto;
import com.lastSchedule.dto.SuggestBoardSearchDto;
import com.lastSchedule.entity.SuggestBoard;
import com.lastSchedule.service.SuggestBoardService;
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

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class SuggestBoardController {
    @GetMapping(value = "/suggestboard/new")
    public String suggestBoardForm(Model model){
        model.addAttribute("suggestBoardFormDto", new SuggestBoardFormDto());

        return "/suggestBoard/suggestBoardInsert";
    }

    private void getGroupNumber() {
    }
    private final SuggestBoardService suggestBoardService;

    @PostMapping(value = "/suggestboard/new")
    public String suggestBoardNew(@Valid SuggestBoardFormDto dto, BindingResult error, Model model){
        if(error.hasErrors()) {
            System.out.println("SuggestBoardController - suggestBoardNew01");

            // 오류 정보 출력
            for (ObjectError objError : error.getAllErrors()) {
                System.out.println(objError);
            }

            return "/suggestBoard/suggestBoardInsert";
        }

        try {
            suggestBoardService.saveSuggestBoard(dto);
            System.out.println("SuggestBoardController - suggestBoardNew03");

        }catch (Exception err){
            err.printStackTrace();
            System.out.println("SuggestBoardController - suggestBoardNew03");

            model.addAttribute("errorMessage", "예외가 발생했습니다.");

            return "/suggestBoard/suggestBoardInsert";
        }
        System.out.println("SuggestBoardController - suggestBoardNew04");
        System.out.println("sb_groupNumber2 : " + dto.getSb_groupNumber());
        return "redirect:/suggestboard/list";
    }

    @GetMapping(value = {"/suggestboard/list", "suggestboard/list/{page}"})
    public String suggestBoardMangestBoard(SuggestBoardSearchDto searchDto, @PathVariable("page") Optional<Integer> page, Model model){
        Pageable pageable = PageRequest.of(page.isPresent() ? page.get() : 0, 5);
        Page<SuggestBoard> suggestBoards = suggestBoardService.getAdminSuggestBoardPage(searchDto, pageable);

        model.addAttribute("suggestBoards", suggestBoards);
        model.addAttribute("searchDto", searchDto) ; //  검색 조건 유지를 위하여
        model.addAttribute("maxPage", 5) ; // 하단에 보여줄 최대 페이지 번호
        return "suggestBoard/suggestBoardList";
    }

    @GetMapping(value = "/suggestboard/detail/{sb_no}")
    public String suggestBoardDetail(HttpServletRequest request, Model model, @PathVariable("sb_no") Long no) {
        System.out.println("SuggestBoardController - suggestBoardDetail01");
        int updatedViewCount = suggestBoardService.incrementViewCount(no);

        SuggestBoardFormDto dto = suggestBoardService.getSuggestBoardDetail(no);
        System.out.println("SuggestBoardController - suggestBoardDetail02");
        model.addAttribute("suggestBoard", dto);
        model.addAttribute("updatedViewCount", updatedViewCount);

        return "suggestBoard/suggestBoardDetail";
    }

    @GetMapping(value = "/suggestboard/{sb_no}")
    public String deGetUpdate(@PathVariable("sb_no") Long no, Model model) {
        try {
            SuggestBoardFormDto dto = suggestBoardService.getSuggestBoardDetail(no);
            model.addAttribute("dto", dto);
        } catch (EntityNotFoundException err) {
            model.addAttribute("errorMessage", "존재하지 않는 이슈입니다.");
            model.addAttribute("dto", new SuggestBoardFormDto());
        }
        return "suggestBoard/suggestBoardUpdate";
    }

    @PostMapping(value = "/suggestboard/{sb_no}")
    public String suggestBoardUpdate(@Valid SuggestBoardFormDto dto, BindingResult error, Model model) {
        String whenError = "/suggestBoard/suggestBoardUpdate";
        System.out.println("SuggestBoardController - suggestBoardUpdate01");

        for (ObjectError objError : error.getAllErrors()) {
            System.out.println(objError);
        }
        model.addAttribute("dto", dto);

        if (error.hasErrors()) {
            System.out.println("SuggestBoardController - suggestBoardUpdate02");
            model.addAttribute("dto", dto);
            return whenError;
        }

        try {
            suggestBoardService.updateSuggestBoard(dto);
        } catch (Exception err) {
            model.addAttribute("errorMessage", "일정 업데이트 중에 오류가 발생하였습니다");
            err.printStackTrace();
            return whenError;
        }
        return "redirect:/suggestboard/list";
    }

    @GetMapping(value = "suggestboard/delete/{no}")
    public String deleteSuggestBoard(@PathVariable("no") Long no) {
        suggestBoardService.deleteSuggestBoard(no);

        return "redirect:/suggestboard/list";
    }
}
