package com.lastSchedule.entity;

import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.SchoolIssueFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "school_issue")
@Getter @Setter @ToString
public class SchoolIssue extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "schoolIssueId")
    private Long schoolIssueId;

    private String event;//개인 일정 제목

    @Column(columnDefinition = "TEXT")// 엔티티의 속성을 정의해줌
    private String description;// 개인 일정 내용

    @Enumerated(EnumType.STRING)
    private Status status; // 프로젝트 진행 상태(해야할것, 하는 중, 완료)

    private String period;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schoolId")
    private School school;

    @Column(name = "calendar_id")
    private Long calendarId;

    private String schoolCode;

    private String remainingPeriod;


    public void updateSchoolIssue(SchoolIssueFormDto dto){
        this.schoolIssueId= dto.getSchoolIssueId();
        this.status = dto.getStatus();
        this.description = dto.getDescription();
        this.event = dto.getEvent();
        this.setStartDate(dto.getStartDate());
        this.setFinishDate(dto.getFinishDate());
    }

    public Long getCreatedAt() {
        return schoolIssueId;
    }
}
