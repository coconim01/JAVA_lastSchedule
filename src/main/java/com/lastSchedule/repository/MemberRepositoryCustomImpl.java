package com.lastSchedule.repository;

import com.lastSchedule.dto.MemberSearchDto;

import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.QMember;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public MemberRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Member> getAdminMemberPage(MemberSearchDto searchDto, Pageable pageable) {
        QueryResults<Member> results = this.queryFactory
                .selectFrom(QMember.member)
                .where(searchByPosition(searchDto.getMem_searchBy(), searchDto.getMem_searchQuery()))
                .orderBy(QMember.member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Member> members = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(members, pageable, total);
    }

    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)) {
            // 둘 다 비어있는 경우, 전체 검색을 수행
            return null;
        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
            return QMember.member.name.like("%" + trimmedQuery + "%")
                    .or(QMember.member.email.like("%" + trimmedQuery + "%"))
                    .or(QMember.member.schoolName.like("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equalsIgnoreCase("user", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QMember.member.user.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("name", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QMember.member.name.likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("email", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QMember.member.email.likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("schoolName", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QMember.member.schoolName.likeIgnoreCase("%" + trimmedQuery + "%");
            }
        }
        return null;
    }
}
