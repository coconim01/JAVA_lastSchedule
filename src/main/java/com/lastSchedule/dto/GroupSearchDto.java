package com.lastSchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class GroupSearchDto {
    private String group_searchDateType;
    private String group_searchBy;
    private String group_searchQuery;
    private String group_searchGroupName;
    private String group_searchMember;
}
