package com.lastSchedule.service;

import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.entity.School;
import com.lastSchedule.entity.SchoolCalendar;
import com.lastSchedule.repository.RedDateRepository;
import com.lastSchedule.repository.SchoolCalendarRepository;
import com.lastSchedule.repository.SchoolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class    SchoolCalendarService {
    @Autowired
    private SchoolCalendarRepository exRepository;
    @Autowired
    private SchoolRepository schoolRepository;
    @Autowired
    private RedDateService redDateService;

    public List<Map<String, Object>> getEventList(Long schoolId) throws IOException {
        //해당 학교 Id를 입력하면 그 학교Id로 calendar를 찾아줘야 한다. 테스트라 수정 해야한다.


        List<SchoolCalendar> SchoolCalendarList = exRepository.findBySchoolSchoolId(schoolId);



        List<Map<String, Object>> eventList = new ArrayList<>();
        eventList.addAll(redDateService.makeRedDateCalendar());
        for (SchoolCalendar Calendar : SchoolCalendarList) {
            Map<String, Object> event = new HashMap<>();

            System.out.println("id : "+ Calendar.getCalendar_id());
            event.put("id", Calendar.getCalendar_id());
            System.out.println("title : "+ Calendar.getTitle());

            System.out.println("url : "+ "/schoolIssue_event/detail/"+Calendar.getCalendar_id());
            event.put("url", "/schoolIssue_event/detail/"+Calendar.getCalendar_id());

            event.put("title", Calendar.getTitle());
            System.out.println("start : "+ Calendar.getStartTime());

            event.put("start", Calendar.getStartTime());
            System.out.println("end : "+ Calendar.getEndTime());

            event.put("end", Calendar.getEndTime());
            System.out.println("color : "+ "#333333");
            event.put("color", "#333333");

            System.out.println("description : "+ Calendar.getDescription()
            );
            event.put("description", Calendar.getDescription());

            System.out.println("importance : "+ "1"   );
            event.put("importance", 1L);

            System.out.println("calendarType : "+ CalendarType.school
            );
            event.put("calendarType", CalendarType.school);

            eventList.add(event);
        }
        return eventList;
    }

    public List<SchoolCalendar> getAllEvents() {
        return exRepository.findAll();
    }

}
