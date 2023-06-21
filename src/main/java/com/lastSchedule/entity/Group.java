package com.lastSchedule.entity;


import com.lastSchedule.dto.GroupFormDto;
import com.lastSchedule.entity.GroupMember;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Entity
@Setter@Getter
@Table(name = "newgroups")
public class Group extends BaseEntity{
    @Id
    @Column(name = "groups_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name="group_color")
    private String group_color;

    //group장
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //그룹 명칭(2-2, 3-4, 선도부 등등)
    private String groupName;

    //참여자 즉 학교면 학생, 동아리면 부원
    @OneToMany(mappedBy = "group", cascade = {CascadeType.ALL, CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<GroupMember> groupMembers = new ArrayList<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Issue> issues = new ArrayList<>();

    private Integer headCount;//총 학생 수


    private String specialNote;//그룹별 특이사항

    @Column(name = "schoolcode")
    private String schoolcode;
//     나중에 해당 학교에 대한 그룹이라는 걸 위한 속성

    public Group() {
    }

    public Group(Long id) {
        this.id = id;
    }



    //goupmember만들기 파트 (Group편)
    public  void addGroupMember(GroupMember groupMember){
        groupMembers.add(groupMember);
        groupMember.setGroup(this);
    }
    //group만들기 파트 (GroupMember편)
    public static Group createGroup(List<GroupMember> groupMemberList, Member member){ //School school 포함하기...
        Group group = new Group();
        for (GroupMember bean: groupMemberList) {
            group.addGroupMember(bean);
        }
        group.setMember(member);
        group.setSchoolcode(member.getSchoolCode());
        group.setGroup_color(group.getRandomPastelColor());
        return group;
    }

    public static final String[] PASTEL_COLORS = {
            "#FFC3A0", "#FFAFB0", "#FFBFD3", "#FFCBF1", "#FFD8FF",
            "#C4FFFF", "#A3FFFF", "#ABF8FF", "#AEE7FF", "#B8DDFF",
            "#B9D9FF", "#BAC5FF", "#C9BAFF", "#E9B8FF", "#FFB5FF",
            "#FFABD4", "#FFA0AE", "#FFA6A0", "#FFB489", "#FFC97E",
            "#D4D4D4", "#BFBFBF", "#ACACAC", "#999999", "#858585"
    };

    public String getRandomPastelColor() {
        Random random = new Random();
        int index = random.nextInt(PASTEL_COLORS.length);
        return PASTEL_COLORS[index];
    }


    public void removeGroupMemberByMember(Member member) {
        GroupMember groupMemberToRemove = null;
        for(GroupMember groupMember : groupMembers){
            if(groupMember.getMember().equals(member)){
                groupMemberToRemove = groupMember;
                break;
            }
        }
        System.out.println("제거되는 그룹 멤버: " + member.getId()); // 디버깅 문장 추가

        if(groupMemberToRemove != null){
            groupMembers.remove(groupMemberToRemove);
        }
    }
}