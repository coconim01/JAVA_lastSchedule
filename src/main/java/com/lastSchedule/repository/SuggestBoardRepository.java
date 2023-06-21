package com.lastSchedule.repository;

import com.lastSchedule.dto.SuggestBoardSearchDto;
import com.lastSchedule.entity.SuggestBoard;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SuggestBoardRepository extends JpaRepository<SuggestBoard, Long>, SuggestBoardRepositoryCustom {
    Page<SuggestBoard> getAdminSuggestBoardPage(SuggestBoardSearchDto searchDto, Pageable pageable);

}


