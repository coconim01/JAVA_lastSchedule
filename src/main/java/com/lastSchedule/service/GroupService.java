package com.lastSchedule.service;

import com.lastSchedule.dto.GroupFormDto;
import com.lastSchedule.dto.GroupSearchDto;
import com.lastSchedule.dto.IssueFormDto;
import com.lastSchedule.dto.MemberSearchDto;
import com.lastSchedule.entity.*;
import com.lastSchedule.repository.GroupCalendarRepository;
import com.lastSchedule.repository.GroupMemberRepository;
import com.lastSchedule.repository.GroupRepository;
import com.lastSchedule.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {
    @Autowired
    GroupRepository groupRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    GroupMemberRepository groupMemberRepository;
    @Autowired
    GroupCalendarRepository groupCalendarRepository;
    @Autowired
    GroupMemberService groupMemberService;
    @Autowired
    MemberService memberService;

    public GroupService(GroupRepository groupRepository, MemberRepository memberRepository, GroupMemberRepository groupMemberRepository, GroupMemberService groupMemberService, MemberService memberService, GroupCalendarRepository groupCalendarRepository) {
        this.groupRepository = groupRepository;
        this.memberRepository = memberRepository;
        this.groupMemberRepository = groupMemberRepository;
        this.groupMemberService = groupMemberService;
        this.memberService = memberService;
        this.groupCalendarRepository = groupCalendarRepository;
    }


    public Long Group(GroupFormDto groupFormdto, Principal principal) {
        // 그룹 생성시...사용되는 서비스
        // 그룹 생성시 해당 그룹에 들어오는 member들 찾기 (Id로)
        List<Member> memberList = new ArrayList<>(); //member 리스트...나중에 써먹을지도 // 20230525 잠깐 지워봄

        //  groupFormdto.getMemberIdList(); //getMemberIdList() 메서드


        List<GroupMember> groupMemberList = new ArrayList<>();
        List<Long> memberIdList = groupFormdto.getMemberIdList();
        Set<Long> memberIdSet = new HashSet<>(memberIdList);
        System.out.println("전달된 멤버 ID 개수 : " + memberIdList.size());


        for (Long memberId : memberIdSet) {
            Member member = memberRepository.findById(memberId)
                    .orElseThrow(EntityNotFoundException::new);

            Group group = Group.createGroup(groupMemberList, member);

            GroupMember groupMember = GroupMember.createGroupMember(member);
            groupMember.setGroup(group);
            //member객체를 id로 찾고(member가 여러개가 들어가니 List로...)
            //해당 Id로 찾은 member의 해당하는 groupmember를 만들고...

            groupMemberList.add(groupMember);
//            memberList.add(member); // 20230525 잠깐 지워봄

        }


        // ////////이 사이에 학교코드를 붙이는 group.setSchoolcode 를 넣을것 ///////////////

        String email = principal.getName();
        Member member = memberRepository.findByEmail(email);

        Group group = Group.createGroup(groupMemberList, member);
        //group 안에 있는 변수인 groupMember를 넣으면서 group 1개의 객체를 완성시킨다.


        //group.setId(groupFormdto.getId());
        //group.setMember(groupFormdto.getMember());

        group.setGroupName(groupFormdto.getGroupName());
        group.setHeadCount(groupFormdto.getHeadCount());
        group.setSpecialNote(groupFormdto.getSpecialNote());


        groupRepository.save(group);

        return group.getId();  //groupid를 리턴
    }

    public Group getGroupById(Long Id) {
        System.out.println("Id : " + Id);
        return groupRepository.findById(Id).orElseThrow(EntityNotFoundException::new);
    }

//    public Page<Group> getAdminGroupPage(List<Group> groups, Pageable pageable, GroupSearchDto searchDto) {
//        List<Long> groupIds = groups.stream().map(Group::getId)
//                .collect(Collectors.toList());
//        return groupRepository.getAdminGroupPage(groupIds, searchDto, pageable);
//    }

    //로그인한 멤버의 그룹 조회
    public List<Group> getGroupsByLoggedInMemberId(Long loggedInMemberId) {
        //로그인한 멤버의 그룹 멤버 조회
        List<GroupMember> loggedInMemberGroupMembers = groupMemberRepository.findByMemberId(loggedInMemberId);

        List<Group> groups = new ArrayList<>();
        for (GroupMember groupMember : loggedInMemberGroupMembers) {
            System.out.println("gorupmember.groupId : " + groupMember.getGroup().getId());
            groups.add(groupMember.getGroup());
        }

        return groups;

    }


    public Long getIdByLoggedInMemberId(Long loggedInMemberId) {
        //로그인한 멤버의 그룹 멤버 조회
        List<GroupMember> loggedInMemberGroupMembers = groupMemberRepository.findByMemberId(loggedInMemberId);

        if (!loggedInMemberGroupMembers.isEmpty()) {
            return loggedInMemberGroupMembers.get(0).getGroup().getId();
        }
        return null;
    }

    //로그인한 멤버의 그룹과 같은 그룹 아이디를 가진, 즉 같은 그룹의 멤버들 조회
    public List<Member> getMembersInSameGroupAsLoggedInMember(Long loggedInMemberId, Long groupId) {

        //로그인한 멤버와 같은 그룹 아이디를 가진 그룹 멤버 조회
        List<GroupMember> groupMembersWithSameGroupId = groupMemberService.getGroupMembersWithSameGroupId(groupId);
        System.out.println("그룹 아이디 " + groupId + "를 가진 그룹 멤버: " + groupMembersWithSameGroupId);

        //그룹 멤버들로부터 멤버들을 추출하여 반환
        List<Member> memberInSameGroup = groupMembersWithSameGroupId.stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toList());

//        //로그인한 멤버를 제외한 그룹 멤버들만 추출
//        memberInSameGroup.removeIf(member -> member.getId().equals(loggedInMemberId));

        return memberInSameGroup;

    }


    public GroupFormDto getGroupDetail(Long id) {
        Group group = groupRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        GroupFormDto dto = GroupFormDto.of(group);

        return dto;
    }

    public Long updateGroupAndGroupMembers(GroupFormDto dto) {
        // 그룹 업데이트
        Group group = groupRepository.findById(dto.getId()).orElseThrow(EntityNotFoundException::new);
        group.setGroupName(dto.getGroupName());
        group.setHeadCount(dto.getHeadCount());
        group.setSpecialNote(dto.getSpecialNote());

        // 그룹 멤버 데이터 업데이트
        if (dto.getExistingMemberIds() != null) {
            List<Member> membersToAdd = new ArrayList<>();
            List<Member> membersToRemove = new ArrayList<>();

            // 기존 그룹 멤버 업데이트
            List<Long> existingMemberIds = new ArrayList<>(dto.getExistingMemberIds()); // 복사본 생성
            for (GroupMember groupMember : group.getGroupMembers()) {
                Long memberId = groupMember.getMember().getId();
                if (!existingMemberIds.contains(memberId)) {
                    membersToRemove.add(groupMember.getMember());
                } else {
                    existingMemberIds.remove(memberId);
                }
            }

            // 기존 그룹 멤버 제거
            for (Member memberToRemove : membersToRemove) {
                System.out.println("제거되는 그룹 멤버: " + memberToRemove.getId());
                group.removeGroupMemberByMember(memberToRemove);
            }

            // 남은 existingMemberIds에 해당하는 멤버는 새로 추가된 멤버일 수 있으므로 추가 처리
            for (Long memberId : existingMemberIds) {
                Member member = memberRepository.findById(memberId).orElseThrow(EntityNotFoundException::new);
                membersToAdd.add(member);
                GroupMember groupMember = GroupMember.createGroupMember(member);
                group.addGroupMember(groupMember);
            }

            System.out.println("existingMemberIds: " + dto.getExistingMemberIds());
            System.out.println("otherMemberIds: " + dto.getOtherMemberIds());
        }


        groupRepository.save(group);

        return group.getId();
    }

    public void deleteGroup(Long id) {
        Group group = groupRepository.findById(id).orElseThrow(null);

        if (group != null) {
            //그룹에 연결된 모든 그룹 캘린더 조회
            List<GroupCalendar> calendars = groupCalendarRepository.findAllByGroup(group);

            if (!calendars.isEmpty()) {
                groupCalendarRepository.deleteAll(calendars);
            }
            groupRepository.delete(group);
        }
    }


    //로그인한 멤버가 그룹장인 그룹 조회
    public List<Group> getMyGroupsByMember(Member member){
        //로그인한 멤버의 그룹 멤버 조회

        return groupRepository.findByMemberId(member.getId());
    }

    public List<Long> getGroupIdsByLoggedInMemberId(Long loggedInMemberId){
        //로그인한 멤버의 그룹 멤버 조회
        List<GroupMember> loggedInMemberGroupMembers = groupMemberRepository.findByMemberId(loggedInMemberId);

        List<Long> groupIds = new ArrayList<>();
        for(GroupMember groupMember: loggedInMemberGroupMembers){
            groupIds.add(groupMember.getGroup().getId());
        }

        return groupIds;
    }

    public List<Member> getRemainingMembers(Long loggedInMemberId, Long groupId, String schoolCode) {

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new EntityNotFoundException("그룹을 찾을 수 없습니다"));
        //그룹에 속한 멤버들 조회
        List<Member> groupMembers = group.getGroupMembers().stream()
                .map(GroupMember::getMember)
                .collect(Collectors.toList());

        //그룹에 속하지 않은 멤버들 조회
        List<Member> allMembers = memberRepository.findAll();
        List<Member> remainingMembers = allMembers.stream()
                .filter(member -> !groupMembers.contains(member))
                .filter(member -> member.getSchoolCode().equals(schoolCode))
                .collect(Collectors.toList());

        return remainingMembers;

    }

    public Page<Group> getAdminGroupPage(GroupSearchDto searchDto, Pageable pageable) {
        return groupRepository.getAdminGroupPage(searchDto, pageable);
    }
}