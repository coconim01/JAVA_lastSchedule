package com.lastSchedule.service;

import com.lastSchedule.entity.GroupMember;
import com.lastSchedule.repository.GroupMemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupMemberService {
    private final GroupMemberRepository groupMemberRepository;

    public GroupMemberService(GroupMemberRepository groupMemberRepository){
        this.groupMemberRepository = groupMemberRepository;
    }

    public List<GroupMember> getGroupMembersByMemberId(Long memberId){
        return groupMemberRepository.findByMemberId(memberId);
    }

    public List<GroupMember> getGroupMembersWithSameGroupId(Long groupId){
        return groupMemberRepository.findByGroupId(groupId);
    }
}
