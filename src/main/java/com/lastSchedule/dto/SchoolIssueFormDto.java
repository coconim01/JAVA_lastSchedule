package com.lastSchedule.dto;

import com.lastSchedule.constant.Status;
import com.lastSchedule.entity.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class SchoolIssueFormDto {
    private Long schoolIssueId;

    private String schoolCode;

    @NotBlank(message = "이벤트명은 필수 입력 사항입니다.")
    private String event;

    @NotBlank(message = "이벤트명은 대한 설명은 필수 입력 사항입니다.")
    private String description;

    private Status status;

    @NotNull(message = "시작일은 필수 입력 사항입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;


    @NotNull(message = "종료일은 필수 입력 사항입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime finishDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateDate;

    private static ModelMapper modelMapper = new ModelMapper();

    public SchoolIssue createEvent(){
        SchoolIssue schoolIssue = new SchoolIssue();
        schoolIssue = modelMapper.map(this, SchoolIssue.class);
        return schoolIssue ;
    }

    public SchoolCalendar createSchoolCalendar(SchoolIssue schoolIssue) {
        SchoolCalendar schoolCalendar = new SchoolCalendar();

        LocalDateTime startDateTime = schoolIssue.getStartDate();
        LocalDateTime endDateTime = schoolIssue.getFinishDate();
        schoolCalendar.setTitle(schoolIssue.getEvent());
        schoolCalendar.setStartTime(startDateTime);
        schoolCalendar.setEndTime(endDateTime);
        schoolCalendar.setDescription(schoolIssue.getDescription());

        return schoolCalendar;
    }

    public static SchoolIssueFormDto of(SchoolIssue personal){return modelMapper.map(personal, SchoolIssueFormDto.class);}


}
