package com.lastSchedule.dto;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import com.lastSchedule.entity.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;


@Getter @Setter
@ToString
public class IssueFormDto {

    private Long id;

    @NotBlank(message = "프로젝트명은 필수 입력 사항입니다.")
    private String title;

    @NotBlank(message = "프로젝트에 대한 설명은 필수 입력 사항입니다.")
    private String description;

    private Priority priority;

    private Status status;

    private String editor;

    private String watcher;

    @NotNull(message = "시작일은 필수 입력 사항입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startDate;

    @NotNull(message = "종료일은 필수 입력 사항입니다.")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime finishDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime updateDate;


    private GroupFormDto groupFormDto;

    private Long group;

    private Long groupId;

    private String groupName;

    private static ModelMapper modelMapper = new ModelMapper();

    public Issue createIssue(){return modelMapper.map(this, Issue.class);}

    public static IssueFormDto of(Issue issue){
        IssueFormDto issueFormDto = new IssueFormDto();
        issueFormDto.setId(issue.getId());
        issueFormDto.setTitle(issue.getTitle());
        issueFormDto.setDescription(issue.getDescription());
        issueFormDto.setPriority(issue.getPriority());
        issueFormDto.setStatus(issue.getStatus());

        issueFormDto.setEditor(issue.getEditor());
        issueFormDto.setWatcher(issue.getWatcher());

        issueFormDto.setGroupId(issue.getGroup().getId());
        issueFormDto.setGroupName(issue.getGroup().getGroupName());
        issueFormDto.setStartDate(issue.getStartDate());
        issueFormDto.setFinishDate(issue.getFinishDate());
        return issueFormDto;}

    public void setGroup(Long groupId) {
        this.groupId = groupId;
    }

    // 그룹 반환 메서드
    public Long getGroup() {
        return group;
    }

    public void setGroupId(Long groupId) {

        this.groupId = groupId;
        this.group = groupId;

    }

    public GroupCalendar createGroupCalendar(Issue issue) {
        GroupCalendar groupCalendar = new GroupCalendar();

        Long important = 4L;
        if (issue.getPriority() == Priority.HIGH) {
            important = 2L;
        } else if (issue.getPriority() == Priority.MEDIUM) {
            important = 3L;
        } else if (issue.getPriority() == Priority.LOW) {
            important = 4L;
        }
        LocalDateTime startDateTime = issue.getStartDate();
        LocalDateTime endDateTime = issue.getFinishDate();
        groupCalendar.setTitle(issue.getTitle());
        groupCalendar.setGroup(issue.getGroup());
        groupCalendar.setStartTime(startDateTime);
        groupCalendar.setEndTime(endDateTime);
        groupCalendar.setImportance(important);
        groupCalendar.setDescription(issue.getDescription());

        return groupCalendar;
    }



}
