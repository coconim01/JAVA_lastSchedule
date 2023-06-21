package com.lastSchedule.service;

import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupCalendar;
import com.lastSchedule.repository.GroupCalendarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GroupCalendarService {
    @Autowired
    private GroupCalendarRepository exRepository;

    public List<Map<String, Object>> getEventList(Group group) {
        List<GroupCalendar> GroupCalendarList =  exRepository.findByGroupId(group.getId());
        List<Map<String, Object>> eventList = new ArrayList<>();
        for (GroupCalendar Calendar : GroupCalendarList) {
            Map<String, Object> event = new HashMap<>();
            System.out.println("groups_id : "+ Calendar.getGroup().getId());
            System.out.println("url : "+ "/group_event/detail/"+Calendar.getCalendar_id());
            event.put("url", "/group_event/detail/"+Calendar.getCalendar_id());

            System.out.println("id : "+ Calendar.getCalendar_id());
            event.put("id", Calendar.getCalendar_id());
            System.out.println("title : "+ Calendar.getTitle());

            event.put("title", Calendar.getTitle());
            System.out.println("start : "+ Calendar.getStartTime());

            event.put("start", Calendar.getStartTime());
            System.out.println("end : "+ Calendar.getEndTime());

            event.put("end", Calendar.getEndTime());
            System.out.println("color : "+ Calendar.getGroup().getGroup_color());
            event.put("color", Calendar.getGroup().getGroup_color());

            System.out.println("importance : "+ "2" );
            event.put("importance", 2L);

            System.out.println("description : "+ Calendar.getDescription()
            );
            event.put("description", Calendar.getDescription());

            System.out.println("calendarType : "+ CalendarType.group
            );
            event.put("calendarType", CalendarType.group);

            eventList.add(event);
        }
        return eventList;
    }

    public List<GroupCalendar> getAllEvents() {
        return exRepository.findAll();
    }

}
