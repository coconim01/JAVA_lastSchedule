package com.lastSchedule.repository;

import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.dto.SuggestBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import com.lastSchedule.entity.SuggestBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SuggestBoardRepositoryCustom {
    Page<SuggestBoard> getAdminSuggestBoardPage(SuggestBoardSearchDto searchDto, Pageable pageable);
}
