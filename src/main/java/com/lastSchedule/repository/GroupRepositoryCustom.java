package com.lastSchedule.repository;

import com.lastSchedule.dto.GroupSearchDto;
import com.lastSchedule.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupRepositoryCustom {
    Page<Group> getAdminGroupPage(GroupSearchDto searchDto, Pageable pageable);
}
