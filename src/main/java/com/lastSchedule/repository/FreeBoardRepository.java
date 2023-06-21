package com.lastSchedule.repository;

import com.lastSchedule.dto.FreeBoardSearchDto;
import com.lastSchedule.entity.FreeBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreeBoardRepository extends JpaRepository<FreeBoard, Long>, FreeBoardRepositoryCustom {
    Page<FreeBoard> getAdminFreeBoardPage(FreeBoardSearchDto searchDto, Pageable pageable);

}


