package com.lastSchedule.repository;

import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.SchoolIssueSearchDto;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.entity.QSchoolIssue;
import com.lastSchedule.entity.QSchoolIssue;
import com.lastSchedule.entity.SchoolIssue;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class SchoolIssueRepositoryCustomImpl implements SchoolIssueRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public SchoolIssueRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<SchoolIssue> getAdminSchoolIssuePage(SchoolIssueSearchDto searchDto, Pageable pageable) {
        QueryResults<SchoolIssue> results = this.queryFactory
                .selectFrom(QSchoolIssue.schoolIssue)
                .where(searchByPosition(searchDto.getSearchBy(), searchDto.getSearchQuery()))
                .orderBy(QSchoolIssue.schoolIssue.schoolIssueId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<SchoolIssue> content = results.getResults();
        long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }

    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)) {
            // 둘 다 비어있는 경우, 전체 검색을 수행
            return null;
        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
            return QSchoolIssue.schoolIssue.event.like("%" + trimmedQuery + "%")
                    .or(QSchoolIssue.schoolIssue.description.like("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equals("EVENT", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QSchoolIssue.schoolIssue.event.like("%" + trimmedQuery + "%");
            } else if (StringUtils.equals("DESCRIPTION", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QSchoolIssue.schoolIssue.description.like("%" + trimmedQuery + "%");
            }
        }

        // 여기까지 도달하는 경우는 해당하는 검색 조건이 없으므로 모든 필드를 검색하도록 처리
        String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
        return QSchoolIssue.schoolIssue.event.like("%" + trimmedQuery + "%")
                .or(QSchoolIssue.schoolIssue.description.like("%" + trimmedQuery + "%"));
    }


}




