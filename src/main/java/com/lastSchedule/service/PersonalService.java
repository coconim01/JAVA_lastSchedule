package com.lastSchedule.service;

import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.dto.PersonalSearchDto;
import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.entity.PersonalCalendar;
import com.lastSchedule.repository.PersonalCalendarRepository;
import com.lastSchedule.repository.PersonalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.io.File;
import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class PersonalService {
    private  final PersonalRepository personalRepository;
    private  final PersonalCalendarRepository personalCalendarRepository;


    public Long savePersonal(@Valid PersonalFormDto dto, Member member)throws Exception{
        Personal personal = dto.createEvent(member);
        PersonalCalendar personalCalendar = dto.createPersonalCalendar(personal);

        PersonalCalendar savedCalendar =  personalCalendarRepository.save(personalCalendar);

        personal.setCalendarId(savedCalendar.getCalendar_id());

        System.out.println("personalCalendar : " +    savedCalendar.toString() );
        personalRepository.save(personal);

        System.out.println("성공했습니다!!  personale의 caledar_id : " +    personal.getCalendarId() );

        return personal.getPersonalId().longValue();
    }

    public Page<Personal> getAdminPersonalPage(PersonalSearchDto searchDto, Pageable pageable) {
        return personalRepository.getAdminPersonalPage(searchDto, pageable);
    }

    public PersonalFormDto getPersonalDetail(Long id) {
        Personal personal = personalRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        PersonalFormDto dto = PersonalFormDto.of(personal);

        return dto;
    }
    public Long updatePersonal(PersonalFormDto dto) throws Exception {
        Personal personal = personalRepository.findById(dto.getPersonalId())
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("personal 확인" + personal.toString());
        System.out.println("personal.calendarId 확인" + personal.getCalendarId());


        PersonalCalendar personalCalendar = personalCalendarRepository.findById(personal.getCalendarId())
                        .orElseThrow(EntityNotFoundException::new);
        System.out.println("캘린더 확인" + personalCalendar.toString());

        personal.updatePersonal(dto);
        personalCalendar.updatePersonalCalendar(personal);

        return personal.getPersonalId();
    }



    public void deletePersonal(Long id) {
        Personal personal = personalRepository.findById(id)
                .orElseThrow(EntityNotFoundException::new);
        Long calendar_id = personal.getCalendarId();
        personalCalendarRepository.deleteById(calendar_id);
        personalRepository.deleteById(id);
    }


    public List<Personal> getAllPersonalList() {
        return personalRepository.findAll();
    }
}
