package com.lastSchedule.service;

import com.lastSchedule.dto.SuggestBoardFormDto;
import com.lastSchedule.dto.SuggestBoardSearchDto;
import com.lastSchedule.entity.SuggestBoard;
import com.lastSchedule.repository.SuggestBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SuggestBoardService {
    private final SuggestBoardRepository suggestBoardRepository;

    public int incrementViewCount(Long no) {
        SuggestBoard suggestBoard = suggestBoardRepository.findById(no).orElseThrow(() -> new EntityNotFoundException("Suggestboard not found"));
        System.out.println("SuggestBoardService - incrementViewCount02");
        int updatedViewCount = suggestBoard.getSb_viewCount() + 1;
        suggestBoard.setSb_viewCount(updatedViewCount);
        System.out.println("SuggestBoardService - incrementViewCount03");
        suggestBoardRepository.save(suggestBoard);

        return updatedViewCount;
    }

    public long saveSuggestBoard(SuggestBoardFormDto dto) throws Exception {
        System.out.println("nb_groupNumber3 : " + dto.getSb_groupNumber());
        SuggestBoard suggestBoard = dto.createSuggestBoard();
        System.out.println("suggestBoard : " + suggestBoard);
        suggestBoardRepository.save(suggestBoard);

        return suggestBoard.getSb_no().longValue();
    }

    public Page<SuggestBoard> getAdminSuggestBoardPage(SuggestBoardSearchDto searchDto, Pageable pageable) {
        System.out.println("SuggestBoardService - getAdminSuggestBoardPage");
        return suggestBoardRepository.getAdminSuggestBoardPage(searchDto, pageable);
    }

    public SuggestBoardFormDto getSuggestBoardDetail(Long no) {
        System.out.println("SuggestBoardService - getSuggestBoardDetail01");
        SuggestBoard suggestBoard = suggestBoardRepository.findById(no).orElseThrow(EntityNotFoundException::new);
        System.out.println("SuggestBoardService - getSuggestBoardDetail02");
        SuggestBoardFormDto dto = SuggestBoardFormDto.of(suggestBoard);

        return dto;
    }

    public Long updateSuggestBoard(SuggestBoardFormDto dto) throws Exception {
        SuggestBoard suggestBoard = suggestBoardRepository.findById(dto.getSb_no()).orElseThrow(EntityNotFoundException::new);

        suggestBoard.updateSuggestBoard(dto);

        return suggestBoard.getSb_no();
    }

    public void deleteSuggestBoard(Long no) {
        suggestBoardRepository.deleteById(no);
    }

    public List<SuggestBoard> getAllSuggestBoards() {
        return suggestBoardRepository.findAll();
    }
}
