package com.lastSchedule.service;

import com.lastSchedule.entity.GroupMember;
import com.lastSchedule.entity.PersonalCalendar;
import com.lastSchedule.repository.GroupMemberRepository;
import com.lastSchedule.repository.MemberRepository;
import com.lastSchedule.repository.PersonalCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
public class PersonalCalendarService {

    @Autowired
    private PersonalCalendarRepository personalCalendarRepository;
    @Autowired
    private GroupMemberRepository groupMemberRepository;
    @Autowired
    private MemberRepository memberRepository;

    public List<PersonalCalendar> schedulesfindAll(Long memberId) {
        List<PersonalCalendar> schedules = personalCalendarRepository.findByMemberId(memberId);
        return schedules;
    }

    public List<GroupMember> getGroupMemberByMemberId(Principal principal) {
        String email = principal.getName();
        Long memberId = memberRepository.findByEmail(email).getId();
        return groupMemberRepository.findByMemberId(memberId);
    }



}
