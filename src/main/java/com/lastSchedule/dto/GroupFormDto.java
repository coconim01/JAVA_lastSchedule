package com.lastSchedule.dto;

import com.lastSchedule.entity.Group;
import com.lastSchedule.entity.GroupMember;
import com.lastSchedule.entity.Member;
import lombok.Getter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class GroupFormDto {

    private Long id;

    private String groupName;

    private Integer headCount;

    private Member member; //작성자=그룹장

    private List<GroupMember> groupMembers; //해당 그룹에 포함된 멤버들(반이면 학생 동아리면 부원 등)

    private List<Group> groups;

    private String specialNote;

    private List<Long> memberIds;

    public List<Long> getMemberIds() {
        if (memberIds == null) {
            memberIds = new ArrayList<>();
        }
        return memberIds;
    }

//    public void setMemberIds(List<Long> memberIds) {
//        this.memberIds = memberIds;
//    }


    public List<Long> getMemberIdList() {
        if (memberIds == null) {
            memberIds = new ArrayList<>();
        }
        System.out.println("추가된 멤버들 : " + memberIds.size());

        return this.memberIds;

    }



    public void setGroup(Group group) {
        this.groups = groups;
    }


    private static ModelMapper modelMapper = new ModelMapper();


    public static GroupFormDto of(Group group){
        GroupFormDto dto = modelMapper.map(group, GroupFormDto.class);
        return dto;}

    private List<Long> existingMemberIds; // 기존 그룹 멤버의 아이디 목록
    private List<Long> otherMemberIds; // 다른 멤버의 아이디 목록

    public GroupFormDto() {
        existingMemberIds = new ArrayList<>();
        otherMemberIds = new ArrayList<>();
    }

    public List<Long> getExistingMemberIds() {
        return existingMemberIds;
    }

    public List<Long> getOtherMemberIds() {
        return otherMemberIds;
    }
}