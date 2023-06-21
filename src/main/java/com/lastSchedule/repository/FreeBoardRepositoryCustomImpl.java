package com.lastSchedule.repository;

import com.lastSchedule.dto.FreeBoardSearchDto;
import com.lastSchedule.entity.FreeBoard;
import com.lastSchedule.entity.QFreeBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class FreeBoardRepositoryCustomImpl implements FreeBoardRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public FreeBoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FreeBoard> getAdminFreeBoardPage(FreeBoardSearchDto searchDto, Pageable pageable) {
        QueryResults<FreeBoard> results = this.queryFactory
                .selectFrom(QFreeBoard.freeBoard)
                .where(searchByPosition(searchDto.getFb_searchBy(), searchDto.getFb_searchQuery()))
                .orderBy(QFreeBoard.freeBoard.fb_no.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<FreeBoard> fb_content = results.getResults();
        Long total = results.getTotal();
        System.out.println("freeBoard - getAdminFreeBoardPage");
        System.out.println("results : " + results);
        return new PageImpl<>(fb_content, pageable, total);
    }

    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)) {
            // 둘 다 비어있는 경우, 전체 검색을 수행
            return null;

        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
            return QFreeBoard.freeBoard.fb_title.like("%" + trimmedQuery + "%")
                    .or(QFreeBoard.freeBoard.fb_writer.like("%" + trimmedQuery + "%"))
                    .or(QFreeBoard.freeBoard.fb_email.like("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equalsIgnoreCase("title", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QFreeBoard.freeBoard.fb_title.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("writer", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QFreeBoard.freeBoard.fb_writer.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("email", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QFreeBoard.freeBoard.fb_email.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            }
        }
        return null;
    }
}
