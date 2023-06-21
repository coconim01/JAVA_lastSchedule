package com.lastSchedule.dto;

import com.lastSchedule.constant.Status;
import lombok.Getter;
import lombok.Setter;

//유효성 검사 넣기
@Getter @Setter
public class SchoolIssueSearchDto {

    private String searchDateType;
    private String searchBy;
    private String searchQuery;
    private Status status;
}
