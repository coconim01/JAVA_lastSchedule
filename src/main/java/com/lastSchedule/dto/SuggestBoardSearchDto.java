package com.lastSchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SuggestBoardSearchDto {
    private String sb_searchDateType;
    private String sb_searchBy;
    private String sb_searchQuery;
    private String sb_searchTitle;
    private String sb_searchName;
    private String sb_searchEmail;
}
