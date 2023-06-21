package com.lastSchedule.service;

import com.lastSchedule.ApiExplorer;
import com.lastSchedule.constant.CalendarType;
import com.lastSchedule.entity.RedDate;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class RedDateService  {

    public List<Map<String, Object>> makeRedDateCalendar() {
        List<RedDate> RedDateList = ApiExplorer.makeRedDate();



        List<Map<String, Object>> eventList = new ArrayList<>();
        for (RedDate schedule : RedDateList) {
            Map<String, Object> event = new HashMap<>();
            System.out.println("id : "+ schedule.getCalendar_id());
            event.put("id", schedule.getCalendar_id());
            System.out.println("title : "+ schedule.getTitle());

            event.put("title", schedule.getTitle());
            System.out.println("start : "+ schedule.getStartTime());

            event.put("start", schedule.getStartTime());
            System.out.println("end : "+ schedule.getEndTime());

            event.put("end", schedule.getEndTime());
            System.out.println("color : "+ "#FF0000");
            event.put("color", "#FF0000");

            System.out.println("importance : "+ schedule.getImportance()
            );
            event.put("importance", schedule.getImportance());

            System.out.println("description : "+ ""
            );
            event.put("description", "쉬는날!");

            System.out.println("calendarType : "+ CalendarType.reddate
            );
            event.put("calendarType", CalendarType.reddate);

            System.out.println("allDay : "+ true
            );
            event.put("allDay", true);

            eventList.add(event);
        }
        return eventList;
    }

}

