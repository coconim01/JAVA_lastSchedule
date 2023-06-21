package com.lastSchedule.repository;

import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupCalendar;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupCalendarRepository extends JpaRepository<GroupCalendar, Long> {
    List<GroupCalendar> findByGroupId(Long groupId);

    List<GroupCalendar> findAllByGroup(Group group);
}
