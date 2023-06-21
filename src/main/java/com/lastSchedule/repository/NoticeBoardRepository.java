package com.lastSchedule.repository;

import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface NoticeBoardRepository extends JpaRepository<NoticeBoard, Long>, NoticeBoardRepositoryCustom {
    Page<NoticeBoard> getAdminNoticeBoardPage(NoticeBoardSearchDto searchDto, Pageable pageable);

    Page<NoticeBoard> findBySchoolCode(String schoolCode, NoticeBoardSearchDto searchDto, Pageable pageable);

}


