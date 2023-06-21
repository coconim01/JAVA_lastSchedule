package com.lastSchedule.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;


@EntityListeners(value = {AuditingEntityListener.class})
@MappedSuperclass
@Getter
@Setter
public class BaseTimeEntity {


    private LocalDateTime startDate;//프로젝트 시작일

    private LocalDateTime finishDate;//프로젝트 마감일


    @CreatedDate //엔터티 생성시 자동으로 시간을 기록하겠습니다.
    @Column(updatable = false) // Entity 수정시 나는 갱신하지 않겠습니다.
    private LocalDateTime regDate;


    @LastModifiedDate // 엔터티 수정시 자동으로 시간을 기록하겠습니다.
    private LocalDateTime updateDate;


}
