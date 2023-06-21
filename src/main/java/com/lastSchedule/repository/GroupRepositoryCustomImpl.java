package com.lastSchedule.repository;

import com.lastSchedule.dto.GroupSearchDto;
import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.QGroup;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityManager;
import java.util.List;

public class GroupRepositoryCustomImpl implements GroupRepositoryCustom {
    private JPAQueryFactory queryFactory;

    private GroupRepositoryCustomImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<Group> getAdminGroupPage(GroupSearchDto searchDto, Pageable pageable) {
        QueryResults<Group> results = this.queryFactory
                .selectFrom(QGroup.group)
                .where(searchByPosition(searchDto.getGroup_searchBy(), searchDto
                        .getGroup_searchQuery()))
                .orderBy(QGroup.group.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();

        List<Group> groups = results.getResults();
        Long total = results.getTotal();
        return new PageImpl<>(groups, pageable, total);
    }

    private BooleanExpression searchByPosition(String searchBy, String searchQuery) {
        if(StringUtils.isEmpty(searchBy) && StringUtils.isEmpty(searchQuery)){
            return null;
        } else if (StringUtils.isEmpty(searchBy)) {
            // searchBy가 비어있는 경우, searchQuery만 사용하여 검색 조건 생성
            String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
            return QGroup.group.id.like("%" + trimmedQuery + "%")
                    .or(QGroup.group.groupName.like("%" + trimmedQuery + "%"))
                    .or(QGroup.group.member.name.like("%" + trimmedQuery + "%"));
        } else {
            // searchBy와 searchQuery 모두 사용하여 검색 조건 생성
            if (StringUtils.equalsIgnoreCase("groupName", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QGroup.group.groupName.stringValue().likeIgnoreCase("%" + trimmedQuery + "%");
            } else if (StringUtils.equalsIgnoreCase("member", searchBy)) {
                String trimmedQuery = searchQuery.trim(); // 검색어 앞뒤 공백 제거
                return QGroup.group.member.name.likeIgnoreCase("%" + trimmedQuery + "%");
            }
        }
        return null;
    }
}
