package com.lastSchedule.repository;

import com.lastSchedule.dto.PersonalSearchDto;
import com.lastSchedule.entity.Personal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PersonalRepositoryCustom {
    Page<Personal> getAdminPersonalPage(PersonalSearchDto searchDto, Pageable pageable);

}
