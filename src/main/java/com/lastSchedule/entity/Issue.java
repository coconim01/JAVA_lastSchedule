package com.lastSchedule.entity;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.IssueFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "issue")
@Getter @Setter
public class Issue extends BaseEntity {
    @Id
    @Column(name = "issue_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id; // 프로젝트 고유번호

    private String title; // 프로젝트 제목

    @Column(columnDefinition = "TEXT")// 엔티티의 속성을 정의해줌
    private String description; // 프로젝트 설명

    @Enumerated(EnumType.STRING)
    private Priority priority; // 프로젝트 우선순위(높음, 중간, 낮음)

    @Enumerated(EnumType.STRING)
    private Status status = Status.TODO; // 프로젝트 진행 상태(해야할것, 하는 중, 완료)

    public Status getStatus() {
        return status == null ? Status.TODO : status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }


    @Column(name = "user_position")
    private String userPosition;

    private String editor; //프로젝트 담당자

    private String watcher;// 프로젝트 참여자

    private String period;// 기간

    @ManyToOne
    @JoinColumn(name = "groups_id")
    private Group group;

    @Column(name = "calendar_id")
    private Long calendarId;

    private String remainingPeriod;

    @Override
    public String toString() {
        return "Issue{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", status=" + status +
                ", userPosition='" + userPosition + '\'' +
                ", editor='" + editor + '\'' +
                ", watcher='" + watcher + '\'' +
                ", period='" + period + '\'' +
                ", groupId=" + (group != null ? group.getId() : null) +
                '}';
    }

    public void updateIssue(IssueFormDto issueFormDto) {

        this.title = issueFormDto.getTitle();
        this.editor = issueFormDto.getEditor();
        this.priority = issueFormDto.getPriority();
        this.status = issueFormDto.getStatus();
        this.description = issueFormDto.getDescription();
        this.setStartDate (issueFormDto.getStartDate());
        this.setFinishDate(issueFormDto.getFinishDate());

    }

    public void setGroup(Group group) {
        this.group = group;
    }
}