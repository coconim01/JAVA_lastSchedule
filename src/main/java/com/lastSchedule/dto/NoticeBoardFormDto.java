package com.lastSchedule.dto;

import com.lastSchedule.entity.NoticeBoard;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class NoticeBoardFormDto {
    private Long nb_no; // 글번호

    private String nb_groupNumber; // 고유번호

    private String nb_writer;// 작성자

    private String nb_email;// 작성자

    @NotBlank(message = "제목은 필수 입력 사항입니다.")
    private String nb_title; // 제목

    @NotBlank(message = "내용은 필수 입력 사항입니다.")
    private String nb_content; // 내용

    private int nb_viewCount; // 조회수


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nb_postDate; // 글등록일

    @NotNull(message = "시작일은 필수 입력 사항입니다.")

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nb_startDate; //시작일

    @NotNull(message = "마감일은 필수 입력 사항입니다.")

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nb_finishDate; //마감일


    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate nb_updateDate; // 수정일


    private String nb_schoolCode;

    private static ModelMapper modelMapper = new ModelMapper();

    public NoticeBoard createNoticeBoard() {
        return modelMapper.map(this, NoticeBoard.class);
    }

    public static NoticeBoardFormDto of(NoticeBoard noticeBoard){
        return modelMapper.map(noticeBoard, NoticeBoardFormDto.class);
    }

}
