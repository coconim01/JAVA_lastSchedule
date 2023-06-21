package com.lastSchedule.repository;

import com.lastSchedule.dto.SchoolTimeBoardDto;
import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.SchoolTimeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolTimeBoardRepository extends JpaRepository<SchoolTimeBoard, Long> {
    SchoolTimeBoard findByMemberId(Long memberId);

}
