package com.lastSchedule.entity;


import com.lastSchedule.constant.User;
import com.lastSchedule.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "members")
@Getter @Setter @ToString
public class Member extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id ;

    private String name ;

    @Column(unique = true)
    private String email ;

    private String password ;

    @Enumerated(EnumType.STRING)
    private User user;

    private String schoolName; //학교명

    private String schoolClass;//반

    private String grade; //학년

    private String groupNumber;
    private String schoolCode;

    // 화면에서 넘어오는 dto와 비번 암호화 객체를 이용하여 Memeber 엔터티 객체 생성하는 메소드
    public static Member createMember(
            MemberFormDto memberFormDto,
            PasswordEncoder passwordEncoder){
        Member member = new Member() ;


        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());

        String password = passwordEncoder.encode(memberFormDto.getPassword()) ;
        member.setPassword(password);
//        member.setPassword(memberFormDto.getPassword());

        member.setUser(memberFormDto.getUser());

        member.setSchoolName(memberFormDto.getSchoolName());
        member.setGrade(memberFormDto.getGrade());
        member.setSchoolClass(memberFormDto.getSchoolClass());
        member.setGroupNumber(memberFormDto.getGroupNumber());
        member.setSchoolCode(memberFormDto.getSchoolCode());

        return member ;
    }
}
