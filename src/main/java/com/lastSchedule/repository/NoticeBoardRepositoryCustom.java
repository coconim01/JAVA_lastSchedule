package com.lastSchedule.repository;

import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NoticeBoardRepositoryCustom {
    Page<NoticeBoard> getAdminNoticeBoardPage(NoticeBoardSearchDto searchDto, Pageable pageable);

}
