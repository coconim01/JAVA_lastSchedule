package com.lastSchedule.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "RedDateCalendar")
@Getter@Setter@ToString
public class RedDate {
    @Id
    @Column(name = "calendar_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long calendar_id;

    @Column(name = "title")
    private String title;

    @Column(name = "start")
    private LocalDateTime startTime;

    @Column(name = "end")
    private LocalDateTime endTime;

    @Column(name = "importance")
    private Long importance; // 중요도 (높음 = 3, 중간 = 2, 낮음 = 1)로 캘린더 배열에 필요함...

    public static RedDate createRedDate(LocalDateTime dateTime, String title){
        RedDate redDate = new RedDate();
        redDate.setTitle(title);
        redDate.setStartTime(dateTime);
        redDate.setEndTime(dateTime);
        redDate.setImportance(1L);
        return redDate;
    }

}

