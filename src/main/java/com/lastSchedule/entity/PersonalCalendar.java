package com.lastSchedule.entity;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.repository.PersonalCalendarRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@ToString
@Table(name = "Personalcalendar")
public class PersonalCalendar {
        @Id
        @Column(name = "calendar_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long calendar_id;

        @Column(name = "title")
        private String title;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "id")
        private Member member;


        @Column(name = "description")
        private String description;

        @Column(name = "start")
        private LocalDateTime startTime;

        @Column(name = "end")
        private LocalDateTime endTime;

        @Column(name = "importance")
        private Long importance; // 중요도 (높음 = 3, 중간 = 2, 낮음 = 1)로 캘린더 배열에 필요함...


        //중요도로 색상을 정해주는 메서드...나중에 setter에 사용할 것!
        public String getEventColor() {
                String color = "";
                switch (importance.intValue()) {
                        case 5:
                                color = "#51cda0"; // 낮은 중요도에 해당하는 색상
                                break;
                        case 4:
                                color = "#ffc107"; // 중간 중요도에 해당하는 색상
                                break;
                        case 3:
                                color = "#f44336"; // 높은 중요도에 해당하는 색상
                                break;
                        default:
                                color = ""; // 중요도가 없을 경우 색상 없음
                }
                return color;
        }

    public void updatePersonalCalendar(Personal personal){
        this.title = personal.getEvent();
        this.member = personal.getMember();
        this.description = personal.getDescription();

        Long important = 6L;
        if (personal.getPriority() == Priority.HIGH) {
            important = 4L;
        } else if (personal.getPriority() == Priority.MEDIUM) {
            important = 5L;
        } else if (personal.getPriority() == Priority.LOW) {
            important = 6L;
        }
        LocalDateTime startDateTime = personal.getStartDate();
        LocalDateTime endDateTime = personal.getFinishDate();

        this.importance = important;
        this.endTime = endDateTime;
        this.startTime = startDateTime;
    }



}