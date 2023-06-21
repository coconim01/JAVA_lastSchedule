package com.lastSchedule.entity;

import com.lastSchedule.dto.NoticeBoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "noticeboards")
@Getter @Setter @ToString
public class NoticeBoard extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private Member member;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "nb_no")
    private Long nb_no; // 글번호

    private String nb_writer;// 작성자

    private String nb_email;// 작성자

    private String nb_title; // 제목

    @Column(columnDefinition = "TEXT")
    private String nb_content; // 내용

    private int nb_viewCount; // 조회수

    private LocalDate nb_postDate; // 글등록일

    private LocalDate nb_startDate; //시작일

    private LocalDate nb_finishDate;//마감일
    
    private String nb_groupNumber; // 고유번호

    private String nb_schoolCode; // 학교번호


    public void updateNoticeBoard(NoticeBoardFormDto dto){
        this.nb_no = dto.getNb_no();
        this.nb_writer = dto.getNb_writer();
        this.nb_email = dto.getNb_email();
        this.nb_title = dto.getNb_title();
        this.nb_content = dto.getNb_content();
        this.nb_viewCount = dto.getNb_viewCount();
        if (dto.getNb_groupNumber() != null) {
            this.member.setGroupNumber(dto.getNb_groupNumber());
        }
        if (dto.getNb_schoolCode() != null) {
            this.member.setSchoolCode(dto.getNb_schoolCode());
        }
        this.setNb_postDate(dto.getNb_postDate());
        this.setNb_startDate(dto.getNb_startDate());
        this.setNb_finishDate(dto.getNb_finishDate());
    }

    public String getSchoolCode() {
        return nb_schoolCode;
    }

    public Long getCreatedAt() {
        return nb_no;
    }
}
