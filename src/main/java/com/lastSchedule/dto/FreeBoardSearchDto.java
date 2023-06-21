package com.lastSchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FreeBoardSearchDto {
    private String fb_searchDateType;
    private String fb_searchBy;
    private String fb_searchQuery;
    private String fb_searchTitle;
    private String fb_searchName;
    private String fb_searchEmail;
}
