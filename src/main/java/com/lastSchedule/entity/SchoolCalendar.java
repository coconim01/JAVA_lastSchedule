package com.lastSchedule.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Schoolcalendar")
public class SchoolCalendar {
        @Id
        @Column(name = "calendar_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long calendar_id;

        @OneToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "schoolId")
        private School school;

        @Column(name = "description")
        private String description;

        @Column(name = "title")
        private String title;

        @Column(name = "start")
        private LocalDateTime startTime;

        @Column(name = "end")
        private LocalDateTime endTime;

        @Column(name = "importance")
        private Long importance;

        public void updateSchoolIssueCalendar(SchoolIssue schoolIssue){
                this.title = schoolIssue.getEvent();
                this.description = schoolIssue.getDescription();

                LocalDateTime startDateTime = schoolIssue.getStartDate();
                LocalDateTime endDateTime = schoolIssue.getFinishDate();

                this.endTime = endDateTime;
                this.startTime = startDateTime;
        }

}