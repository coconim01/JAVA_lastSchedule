package com.lastSchedule.service;


import com.lastSchedule.dto.FreeBoardFormDto;
import com.lastSchedule.dto.FreeBoardSearchDto;
import com.lastSchedule.entity.FreeBoard;
import com.lastSchedule.entity.SuggestBoard;
import com.lastSchedule.repository.FreeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FreeBoardService {
    private final FreeBoardRepository freeBoardRepository;

    public int incrementViewCount(Long no) {
        FreeBoard freeBoard = freeBoardRepository.findById(no).orElseThrow(() -> new EntityNotFoundException("Freeboard not found"));
        int updatedViewCount = freeBoard.getFb_viewCount() +1;
        freeBoard.setFb_viewCount(updatedViewCount);
        freeBoardRepository.save(freeBoard);

        return updatedViewCount;
    }

    public long saveFreeBoard(@Valid FreeBoardFormDto dto) throws Exception {
        System.out.println("fb_groupNumber3 : " + dto.getFb_groupNumber());
        FreeBoard freeBoard = dto.createFreeBoard();
        System.out.println("freeBoard : " + freeBoard);
        freeBoardRepository.save(freeBoard);

        return freeBoard.getFb_no().longValue();
    }

    public Page<FreeBoard> getAdminFreeBoardPage(FreeBoardSearchDto searchDto, Pageable pageable) {
        return freeBoardRepository.getAdminFreeBoardPage(searchDto, pageable);
    }

    public FreeBoardFormDto getFreeBoardDetail(Long no) {
        FreeBoard freeBoard = freeBoardRepository.findById(no).orElseThrow(EntityNotFoundException::new);

        FreeBoardFormDto dto = FreeBoardFormDto.of(freeBoard);

        return dto;
    }

    public Long updateFreeBoard(FreeBoardFormDto dto) throws Exception {
        FreeBoard freeBoard = freeBoardRepository.findById(dto.getFb_no()).orElseThrow(EntityNotFoundException::new);

        freeBoard.updateFreeBoard(dto);

        return freeBoard.getFb_no();
    }

    public void deleteFreeBoard(Long no) {
        freeBoardRepository.deleteById(no);
    }

    public List<FreeBoard> getAllFreeBoards() {
        return freeBoardRepository.findAll();
    }
}
