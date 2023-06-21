package com.lastSchedule.service;

import com.lastSchedule.dto.SchoolIssueFormDto;
import com.lastSchedule.dto.SchoolIssueSearchDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.SchoolCalendarRepository;
import com.lastSchedule.repository.SchoolIssueRepository;
import com.lastSchedule.repository.SchoolRepository;
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
public class SchoolIssueService {
    private  final SchoolIssueRepository schoolIssueRepository;
    private  final SchoolCalendarRepository schoolCalendarRepository;
    private final SchoolRepository schoolRepository;


    public Long saveSchoolIssue(@Valid SchoolIssueFormDto dto, Member member)throws Exception{
        SchoolIssue schoolIssue = dto.createEvent();
        SchoolCalendar schoolCalendar = dto.createSchoolCalendar(schoolIssue);
        String schoolcode = member.getSchoolCode();
        School school = schoolRepository.findBySchoolCode(schoolcode);

        schoolCalendar.setSchool(school);
        SchoolCalendar savedCalendar =  schoolCalendarRepository.save(schoolCalendar);

        schoolIssue.setCalendarId(savedCalendar.getCalendar_id());

        System.out.println("SchoolCalendar : " +    savedCalendar.toString() );
        schoolIssue.setSchool(school);
        schoolIssueRepository.save(schoolIssue);

        System.out.println("성공했습니다!!  personale의 caledar_id : " +    schoolIssue.getCalendarId() );

        return schoolIssue.getSchoolIssueId().longValue();
    }

    public Page<SchoolIssue> getAdminSchoolIssuePage(SchoolIssueSearchDto searchDto, Pageable pageable) {

        return schoolIssueRepository.getAdminSchoolIssuePage(searchDto, pageable);
    }

    public SchoolIssueFormDto getSchoolIsseuDetail(Long id) {
        SchoolIssue schoolIssue = schoolIssueRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        SchoolIssueFormDto dto = SchoolIssueFormDto.of(schoolIssue);
        System.out.println("dto : "+dto );

        return dto;
    }
    public Long updateSchoolIssue(SchoolIssueFormDto dto) throws Exception {
        SchoolIssue schoolIssue = schoolIssueRepository.findById(dto.getSchoolIssueId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("SchoolIssue 확인" + schoolIssue.toString());
        System.out.println("schoolIssue.calendarId 확인" + schoolIssue.getCalendarId());


        SchoolCalendar schoolCalendar = schoolCalendarRepository.findById(schoolIssue.getCalendarId())
                        .orElseThrow(EntityNotFoundException::new);
        System.out.println("캘린더 확인" + schoolCalendar.toString());

        schoolIssue.updateSchoolIssue(dto);
        schoolCalendar.updateSchoolIssueCalendar(schoolIssue);

        return schoolIssue.getSchoolIssueId();
    }



    public void deletesShoolIssue(Long id) {
        SchoolIssue schoolIssue = schoolIssueRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Long calendar_id = schoolIssue.getCalendarId();
        schoolCalendarRepository.deleteById(calendar_id);
        schoolIssueRepository.deleteById(id);
    }

    public List<SchoolIssue> getAllSchoolIssues(){return schoolIssueRepository.findAll();}

    public List<SchoolIssue> getIssuesBySchoolCode(String schoolCode) {
        System.out.println("출력될 그룹 아이디 : " + schoolCode);
        return schoolIssueRepository.findBySchoolCode(schoolCode);
    }
}
