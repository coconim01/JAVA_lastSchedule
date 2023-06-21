package com.lastSchedule.entity;

import com.lastSchedule.dto.SuggestBoardFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "suggestBoards")
@Getter @Setter @ToString
public class SuggestBoard extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "name")
    private Member member;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "sb_no")
    private Long sb_no;

    private String sb_groupNumber ;

    private String sb_writer ;

    private String sb_email ;

    private String sb_title ;

    @Column(columnDefinition = "TEXT")
    private String sb_content ;

    private int sb_viewCount ;

    private LocalDate sb_postDate;

    private String sb_schoolCode; // 학교번호

    public void updateSuggestBoard(SuggestBoardFormDto dto) {
        this.sb_no = dto.getSb_no();
        this.sb_title = dto.getSb_title() ;
        this.sb_writer = dto.getSb_writer() ;
        this.sb_email = dto.getSb_email() ;
        this.sb_content = dto.getSb_content() ;
        this.sb_postDate = dto.getSb_postDate() ;
        this.sb_viewCount = dto.getSb_viewCount() ;
        if (dto.getSb_groupNumber() != null) {
            this.member.setGroupNumber(dto.getSb_groupNumber());
        }
    }
}
