package com.lastSchedule.repository;

import com.lastSchedule.dto.GroupSearchDto;
import com.lastSchedule.entity.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long>, GroupRepositoryCustom {
    Optional<Group> findById(Long Id);

    Page<Group> getAdminGroupPage(GroupSearchDto searchDto, Pageable pageable);

    List<Group>  findByMemberId(Long id);
}
