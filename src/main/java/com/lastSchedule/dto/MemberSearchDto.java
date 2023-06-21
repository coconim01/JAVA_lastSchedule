package com.lastSchedule.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberSearchDto {
    private String mem_searchBy;
    private String mem_searchQuery;
    private String mem_searchUser;
    private String mem_searchName;
    private String mem_searchId;
    private String mem_searchEmail;
    private String mem_searchSchoolName;
    // 필요한 다른 검색 가능한 필드 추가
}
