package com.lastSchedule.repository;

import com.lastSchedule.dto.FreeBoardSearchDto;
import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.FreeBoard;
import com.lastSchedule.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreeBoardRepositoryCustom {
    Page<FreeBoard> getAdminFreeBoardPage(FreeBoardSearchDto searchDto, Pageable pageable);
}
