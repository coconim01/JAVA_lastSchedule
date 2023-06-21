package com.lastSchedule.dto;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.entity.PersonalCalendar;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter
@ToString
public class PersonalFormDto {
    private Long personalId;

    @NotBlank(message = "이벤트명은 필수 입력 사항입니다.")
    private String event;

    @NotBlank(message = "이벤트명은 대한 설명은 필수 입력 사항입니다.")
    private String description;

    private Priority priority; // 프로젝트 우선순위(높음, 중간, 낮음)

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

    public Personal createEvent(Member member){
        Personal personal = new Personal();
        personal = modelMapper.map(this, Personal.class);
        personal.setMember(member);
        return personal ;
    }

    public PersonalCalendar createPersonalCalendar(Personal personal) {
        PersonalCalendar personalCalendar = new PersonalCalendar();

        Long important = 6L;
        if (personal.getPriority() == Priority.HIGH) {
            important = 4L;
        } else if (personal.getPriority() == Priority.MEDIUM) {
            important = 5L;
        } else if (personal.getPriority() == Priority.LOW) {
            important = 6L;
        }
        LocalDateTime startDateTime = personal.getStartDate();
        LocalDateTime endDateTime = personal.getFinishDate();
        personalCalendar.setTitle(personal.getEvent());
        personalCalendar.setMember(personal.getMember());
        personalCalendar.setStartTime(startDateTime);
        personalCalendar.setEndTime(endDateTime);
        personalCalendar.setImportance(important);
        personalCalendar.setDescription(personal.getDescription());

        return personalCalendar;
    }

    public static PersonalFormDto of(Personal personal){return modelMapper.map(personal, PersonalFormDto.class);}


}
