package com.lastSchedule.repository;

import com.lastSchedule.dto.MemberSearchDto;
import com.lastSchedule.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepositoryCustom {
    Page<Member> getAdminMemberPage(MemberSearchDto searchDto, Pageable pageable);
}
