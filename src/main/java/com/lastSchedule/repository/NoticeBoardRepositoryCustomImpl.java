package com.lastSchedule.repository;

import com.lastSchedule.dto.NoticeBoardSearchDto;
import com.lastSchedule.entity.NoticeBoard;
import com.lastSchedule.entity.QNoticeBoard;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class NoticeBoardRepositoryCustomImpl implements NoticeBoardRepositoryCustom {
    private JPAQueryFactory queryFactory;

    public NoticeBoardRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<NoticeBoard> getAdminNoticeBoardPage(NoticeBoardSearchDto searchDto, Pageable pageable) {
        QueryResults<NoticeBoard> results = this.queryFactory
                .selectFrom(QNoticeBoard.noticeBoard)
                .where(searchByPosition(searchDto.getNb_searchBy(), searchDto.getNb_searchQuery()))
                .orderBy(QNoticeBoard.noticeBoard.nb_no.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<NoticeBoard> nb_content = results.getResults();
        Long total = results.getTotal();
        System.out.println("err11");
        System.out.println("results : " + results);
        return new PageImpl<>(nb_content, pageable, total);
    }

    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)) {
            // 둘 다 비어있는 경우, 전체 검색을 수행
            return null;

        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
            return QNoticeBoard.noticeBoard.nb_title.stringValue().likeIgnoreCase("%" + trimmedQuery + "%")
                    .or(QNoticeBoard.noticeBoard.nb_writer.stringValue().likeIgnoreCase("%" + trimmedQuery + "%"))
                    .or(QNoticeBoard.noticeBoard.nb_email.stringValue().likeIgnoreCase("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equalsIgnoreCase("title", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QNoticeBoard.noticeBoard.nb_title.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("writer", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QNoticeBoard.noticeBoard.nb_writer.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("email", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QNoticeBoard.noticeBoard.nb_email.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            }
        }
        return null;
    }
}
