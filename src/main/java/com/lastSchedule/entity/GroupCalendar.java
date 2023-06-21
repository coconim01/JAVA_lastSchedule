package com.lastSchedule.entity;

import com.lastSchedule.constant.Priority;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Groupcalendar")
public class GroupCalendar {
        @Id
        @Column(name = "calendar_id")
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long calendar_id;

        @Column(name = "title")
        private String title;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "groups_id", nullable = false)
        private Group group;

        @Column(name = "description")
        private String description;

        @Column(name = "start")
        private LocalDateTime startTime;

        @Column(name = "end")
        private LocalDateTime endTime;

        @Column(name = "importance")
        private Long importance; // 중요도 (높음 = 3, 중간 = 2, 낮음 = 1)로 캘린더 배열에 필요함...하지만 2로 고정했죠~


        public void updateGroupCalendar(Issue issue) {
                this.title = issue.getTitle();
                this.group = issue.getGroup();
                this.description = issue.getDescription();

                Long important = 6L;
                if (issue.getPriority() == Priority.HIGH) {
                        important = 4L;
                } else if (issue.getPriority() == Priority.MEDIUM) {
                        important = 5L;
                } else if (issue.getPriority() == Priority.LOW) {
                        important = 6L;
                }
                LocalDateTime startDateTime = issue.getStartDate();
                LocalDateTime endDateTime = issue.getFinishDate();

                this.importance = important;
                this.endTime = endDateTime;
                this.startTime = startDateTime;
        }
}
