package com.lastSchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class NoticeBoardSearchDto {
    private String nb_searchDateType;
    private String nb_searchBy;
    private String nb_searchQuery;
    private String nb_searchTitle;
    private String nb_searchName;
    private String nb_searchEmail;

    private String schoolCode;
}
