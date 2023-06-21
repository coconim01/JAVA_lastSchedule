package com.lastSchedule.entity;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "group_member")
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "groups_id")
    private Group group;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    //goupmember만들기 파트 (Member편, 완성)
    public static GroupMember createGroupMember(Member member){
        GroupMember groupMember = new GroupMember();
        groupMember.setMember(member);

        return groupMember;
    }
}
