package com.lastSchedule.entity;


import com.lastSchedule.dto.FreeBoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "freeboards")
@Getter @Setter @ToString
public class FreeBoard extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private Member member;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "fb_no")
    private Long fb_no;

    private String fb_writer ;

    private String fb_email ;

    private String fb_title ;

    @Column(columnDefinition = "TEXT")
    private String fb_content ;

    private int fb_viewCount ;

    private LocalDate fb_postDate ;

    private String fb_groupNumber ;

    private String fb_schoolCode; // 학교번호


    public void updateFreeBoard(FreeBoardFormDto dto) {
        this.fb_no = dto.getFb_no();
        this.fb_writer = dto.getFb_writer() ;
        this.fb_email  = dto.getFb_email () ;
        this.fb_title = dto.getFb_title() ;
        this.fb_content = dto.getFb_content() ;
        this.fb_viewCount = dto.getFb_viewCount() ;
        if (dto.getFb_groupNumber() != null) {
            this.member.setGroupNumber(dto.getFb_groupNumber());
        }
        this.setFb_postDate(dto.getFb_postDate()) ;
    }
}
