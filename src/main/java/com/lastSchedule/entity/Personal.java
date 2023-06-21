package com.lastSchedule.entity;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.PersonalFormDto;
import com.lastSchedule.repository.PersonalCalendarRepository;
import com.lastSchedule.repository.PersonalRepository;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "personal")
@Getter @Setter @ToString
public class Personal extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "personalId")
    private Long personalId;

    private String event;//개인 일정 제목

    @Column(columnDefinition = "TEXT")// 엔티티의 속성을 정의해줌
    private String description;// 개인 일정 내용

    @Enumerated(EnumType.STRING)
    private Priority priority; // 프로젝트 우선순위(높음, 중간, 낮음)

    @Enumerated(EnumType.STRING)
    private Status status; // 프로젝트 진행 상태(해야할것, 하는 중, 완료)

    private String period;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id")
    private Member member;

    @Column(name = "calendar_id")
    private Long calendarId;

    public void updatePersonal(PersonalFormDto dto){
        this.personalId= dto.getPersonalId();
        this.status = dto.getStatus();
        this.priority = dto.getPriority();
        this.description = dto.getDescription();
        this.event = dto.getEvent();
        this.setStartDate(dto.getStartDate());
        this.setFinishDate(dto.getFinishDate());
    }


}
