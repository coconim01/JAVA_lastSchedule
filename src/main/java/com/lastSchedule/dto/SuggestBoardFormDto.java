package com.lastSchedule.dto;

import com.lastSchedule.entity.NoticeBoard;
import com.lastSchedule.entity.SuggestBoard;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
public class SuggestBoardFormDto {

    private Long sb_no;

    private String sb_groupNumber ;

    @NotBlank(message = "작성자를 반드시 입력해주세요.")
    private String sb_writer ;

    private String sb_email ;

    @NotBlank(message = "글 제목을 반드시 입력해주세요.")
    private String sb_title ;

    @NotBlank(message = "글 내용을 반드시 입력해주세요.")
    @Column(columnDefinition = "TEXT")
    private String sb_content ;

    private int sb_viewCount ;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate sb_postDate ;

    private String sb_schoolCode;

    private static ModelMapper modelMapper = new ModelMapper();

    public SuggestBoard createSuggestBoard() {
        return modelMapper.map(this, SuggestBoard.class);
    }

    public static SuggestBoardFormDto of(SuggestBoard suggestBoard){
        System.out.println("SuggestBoardFormDto - 0f");
        return modelMapper.map(suggestBoard, SuggestBoardFormDto.class);
    }
}

