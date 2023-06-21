package com.lastSchedule.repository;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;
import com.lastSchedule.dto.PersonalSearchDto;
import com.lastSchedule.entity.Personal;
import com.lastSchedule.entity.QPersonal;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class PersonalRepositoryCustomImpl implements PersonalRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public PersonalRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Personal> getAdminPersonalPage(PersonalSearchDto searchDto, Pageable pageable) {
        QueryResults<Personal> results = this.queryFactory
                .selectFrom(QPersonal.personal)
                .where(searchByPosition(searchDto.getSearchBy(), searchDto.getSearchQuery()),

                        searchByPriority(String.valueOf(searchDto.getPriority()), searchDto.getPriority()
                        ))
                .orderBy(QPersonal.personal.personalId.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Personal> content = results.getResults();
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
            return QPersonal.personal.event.like("%" + trimmedQuery + "%")
                    .or(QPersonal.personal.description.like("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equals("EVENT", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QPersonal.personal.event.like("%" + trimmedQuery + "%");
            } else if (StringUtils.equals("DESCRIPTION", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QPersonal.personal.description.like("%" + trimmedQuery + "%");
            }
        }

        // 여기까지 도달하는 경우는 해당하는 검색 조건이 없으므로 모든 필드를 검색하도록 처리
        String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
        return QPersonal.personal.event.like("%" + trimmedQuery + "%")
                .or(QPersonal.personal.description.like("%" + trimmedQuery + "%"));
    }

    private BooleanExpression searchByPriority(String searchBy, Priority priority) {
        if (priority == null) {
            return null;
        } else if (priority == Priority.HIGH) {
            return QPersonal.personal.priority.eq(Priority.HIGH);
        } else if (priority == Priority.MEDIUM) {
            return QPersonal.personal.priority.eq(Priority.MEDIUM);
        } else if (priority == Priority.LOW) {
            return QPersonal.personal.priority.eq(Priority.LOW);
        }
        return null;
    }
}




