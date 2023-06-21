package com.lastSchedule.service;

import com.lastSchedule.dto.NoticeBoardFormDto;
import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import com.lastSchedule.repository.NoticeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class NoticeBoardService {
    private final NoticeBoardRepository noticeBoardRepository;

//    public void incrementViewCount(Long no) {
//        NoticeBoard noticeBoard = noticeBoardRepository.findById(no)
//                .orElseThrow(() -> new EntityNotFoundException("Notice board not found"));
//
//        noticeBoard.setNb_viewCount(noticeBoard.getNb_viewCount() + 1);
//        noticeBoardRepository.save(noticeBoard);
//    }

    public int incrementViewCount(Long no) {
        NoticeBoard noticeBoard = noticeBoardRepository.findById(no)
                .orElseThrow(() -> new EntityNotFoundException("Noticeboard not found"));

        int updatedViewCount = noticeBoard.getNb_viewCount() + 1;
        noticeBoard.setNb_viewCount(updatedViewCount);
        noticeBoardRepository.save(noticeBoard);

        return updatedViewCount;
    }


    public long saveNoticeBoard(@Valid NoticeBoardFormDto dto) throws Exception {
        System.out.println("nb_groupNumber3 : " + dto.getNb_groupNumber());
        System.out.println("nb_email : " + dto.getNb_email());
        NoticeBoard noticeBoard = dto.createNoticeBoard();
        System.out.println("noticeBoard : " + noticeBoard);
        noticeBoardRepository.save(noticeBoard);

        return noticeBoard.getNb_no().longValue();
    }

    public Page<NoticeBoard> getAdminNoticeBoardPage(NoticeBoardSearchDto searchDto, Pageable pageable) {
        System.out.println("err12");
        return noticeBoardRepository.getAdminNoticeBoardPage(searchDto, pageable);
    }

    public List<NoticeBoard> getAllNoticeBoards() {
        return noticeBoardRepository.findAll();
    }

    public NoticeBoardFormDto getNoticeBoardDetail(Long no){
        NoticeBoard noticeBoard = noticeBoardRepository
                .findById(no)
                .orElseThrow(EntityNotFoundException::new);

        NoticeBoardFormDto dto = NoticeBoardFormDto.of(noticeBoard);

        return dto;
    }

    public Long updateNoticeBoard(NoticeBoardFormDto dto) throws Exception {
        NoticeBoard noticeBoard = noticeBoardRepository.findById(dto.getNb_no())
                .orElseThrow(EntityNotFoundException::new);

        noticeBoard.updateNoticeBoard(dto);

        return noticeBoard.getNb_no();
    }

    public void deleteNoticeBoard(Long no) {
        noticeBoardRepository.deleteById(no);
    }
}
