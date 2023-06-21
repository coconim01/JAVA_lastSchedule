package com.lastSchedule.dto;

import com.lastSchedule.entity.FreeBoard;
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
public class FreeBoardFormDto {

    private Long fb_no;

    private String fb_groupNumber ;

    private String fb_schoolCode;

    @NotBlank(message = "작성자를 반드시 입력해주세요.")
    private String fb_writer ;
    private String fb_email  ;

    @NotBlank(message = "글 제목을 반드시 입력해주세요.")
    private String fb_title ;

    @NotBlank(message = "글 내용을 반드시 입력해주세요.")
    @Column(columnDefinition = "TEXT")
    private String fb_content ;

    private int fb_viewCount ;

    @FutureOrPresent
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate fb_postDate;

    private static ModelMapper modelMapper = new ModelMapper();

    public FreeBoard createFreeBoard() {
        return modelMapper.map(this, FreeBoard.class);
    }

    public static FreeBoardFormDto of(FreeBoard freeBoard){
        return modelMapper.map(freeBoard, FreeBoardFormDto.class);
    }
}
