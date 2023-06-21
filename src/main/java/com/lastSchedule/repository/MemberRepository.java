package com.lastSchedule.repository;

import com.lastSchedule.dto.MemberSearchDto;
import com.lastSchedule.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
    // 이메일을 이용하여 회원을 검색하기 하기 위한 쿼리 메소드입니다.
    Member findByEmail(String email) ;

    Optional<Member> findById(Long id);

    Page<Member> getAdminMemberPage(MemberSearchDto searchDto, Pageable pageable);

    List<Member> findByUserAndEmail(Member user, String email);

    List<Member> findBySchoolCode(String schoolCode);
}
