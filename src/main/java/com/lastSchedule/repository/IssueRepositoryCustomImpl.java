package com.lastSchedule.repository;

import com.lastSchedule.constant.Priority;
import com.lastSchedule.constant.Status;

import com.lastSchedule.dto.IssueSearchDto;
import com.lastSchedule.entity.Issue;
import com.lastSchedule.entity.QGroup;
import com.lastSchedule.entity.QIssue;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;

public class IssueRepositoryCustomImpl implements IssueRepositoryCustom {

    private JPAQueryFactory queryFactory;

    public IssueRepositoryCustomImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }


    @Override
    public Page<Issue> getAdminIssuePage(IssueSearchDto searchDto, Long groupId, Pageable pageable) {

        BooleanExpression groupPredicate = QGroup.group.id
                .eq(groupId);

        QueryResults<Issue> results = this.queryFactory
                .selectFrom(QIssue.issue)
                .join(QIssue.issue.group, QGroup.group)
                .where(groupPredicate,
                        searchByPosition(searchDto.getSearchBy(), searchDto.getSearchQuery()),

                        searchByStatus(String.valueOf(searchDto.getStatus()), searchDto.getStatus()),

                        searchByPriority(String.valueOf(searchDto.getPriority()), searchDto.getPriority()
                                ))
                .orderBy(QIssue.issue.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Issue> content = results.getResults();
        Long total = results.getTotal();

        return new PageImpl<>(content, pageable, total);
    }


    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if (StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)) {
            // 둘 다 비어있는 경우, 전체 검색을 수행
            return null;
        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            return QIssue.issue.title.like("%" + searchQuery + "%")
                    .or(QIssue.issue.editor.like("%" + searchQuery + "%"));
        } else if (StringUtils.isEmpty(searchQuery)) {
            // searchQuery가 비어있는 경우, searchBy만 사용하여 검색 조건 생성
            if (StringUtils.equals("TITLE", searchBy)) {
                return QIssue.issue.title.like("%" + searchQuery + "%");
            } else if (StringUtils.equals("EDITOR", searchBy)) {
                return QIssue.issue.editor.like("%" + searchQuery + "%");
            }
        } else {
            // 둘 다 비어있지 않은 경우, searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equals("TITLE", searchBy)) {
                return QIssue.issue.title.like("%" + searchQuery + "%");
            } else if (StringUtils.equals("EDITOR", searchBy)) {
                return QIssue.issue.editor.like("%" + searchQuery + "%");
            }
        }

    /// 여기까지 도달하는 경우는 해당하는 검색 조건이 없으므로 모든 필드를 검색하도록 처리
        String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
        return QIssue.issue.title.like("%" + trimmedQuery + "%")
                .or(QIssue.issue.editor.like("%" + trimmedQuery + "%"));
    }

    private BooleanExpression searchByStatus(String searchBy, Status status) {
        if (status == null) {
            return null;
        } else if (status == Status.TODO) {
            return QIssue.issue.status.eq(Status.TODO);
        } else if (status == Status.DOING) {
            return QIssue.issue.status.eq(Status.DOING);
        } else if (status == Status.DONE) {
            return QIssue.issue.status.eq(Status.DONE);
        }
        return null;
    }

    private BooleanExpression searchByPriority(String searchBy, Priority priority) {
        if (priority == null) {
            return null;
        } else if (priority == Priority.HIGH) {
            return QIssue.issue.priority.eq(Priority.HIGH);
        } else if (priority == Priority.MEDIUM) {
            return QIssue.issue.priority.eq(Priority.MEDIUM);
        } else if (priority == Priority.LOW) {
            return QIssue.issue.priority.eq(Priority.LOW);
        }
        return null;
    }
}





