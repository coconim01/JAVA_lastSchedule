package com.lastSchedule.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@ToString@Getter@Setter
public class School {

    @Id
    @Column(name = "schoolId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long schoolId;

    @Column(name = "school_name")
    private String school_name;


    @Column(name = "schoolCode")
    private String schoolCode;

    //school Entity를 생성하여 db에 저장해 두지만
    //우리가 할 떈 미리 저장해 둔 데이터로 쓸거다...

}
