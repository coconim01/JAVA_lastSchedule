package com.lastSchedule.service;

import com.lastSchedule.dto.SchoolIssueFormDto;
import com.lastSchedule.dto.SchoolTimeBoardDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.SchoolTimeBoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@Service
@RequiredArgsConstructor
@Transactional
public class SchoolTimeBoardService {
    @Autowired
    private SchoolTimeBoardRepository schoolTimeBoardRepository;


    public Long saveTimeBoard(@Valid SchoolTimeBoardDto dto, Member member) {
        SchoolTimeBoard schoolTimeBoard = dto.updateSchoolTimeBoard();
        schoolTimeBoard.setMember(member);
        schoolTimeBoardRepository.save(schoolTimeBoard);


        System.out.println("성공했습니다!!  schoolTimeBoard  : " +    schoolTimeBoard.getTimeBoardId() );

        return schoolTimeBoard.getTimeBoardId().longValue();
    }

    public SchoolTimeBoard createTimeBoard(Member member) {
        SchoolTimeBoard schoolTimeBoard = new SchoolTimeBoard();
        schoolTimeBoard= schoolTimeBoard.newschoolTimeBoard();
        SchoolTimeBoard newTimeBoard = schoolTimeBoardRepository.save(schoolTimeBoard);
        newTimeBoard.setMember(member);
        System.out.println(" 시간표 확인용 id: " + newTimeBoard.getTimeBoardId());
        System.out.println(" 시간표 확인용 memberid : " + newTimeBoard.getMember().getId());
        return newTimeBoard;
    }

    public Long updateTimeBoard(SchoolTimeBoardDto dto)throws Exception {
        SchoolTimeBoard schoolTimeBoard = schoolTimeBoardRepository.findById(dto.getTimeBoardId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("schoolTimeBoard 확인" + schoolTimeBoard.toString());
        System.out.println("schoolTimeBoard.MemberId 확인" + schoolTimeBoard.getMember().getId());

        schoolTimeBoard.updateSchoolTimeBoard(dto);
        return schoolTimeBoard.getTimeBoardId();
    }
}
