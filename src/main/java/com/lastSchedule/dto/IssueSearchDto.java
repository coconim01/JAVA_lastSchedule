package com.lastSchedule.dto;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Enumerated;
import java.security.Principal;

@Getter @Setter
public class IssueSearchDto {


    private String searchBy;
    private String searchQuery;
    private String title;
    private Priority priority;
    private Status status;
    private Status editor;

    private Long groupId;

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
