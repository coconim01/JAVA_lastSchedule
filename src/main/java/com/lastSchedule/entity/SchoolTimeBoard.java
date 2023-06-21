package com.lastSchedule.entity;

import com.lastSchedule.dto.SchoolTimeBoardDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "school_time_board")
@Getter@Setter
@ToString
public class SchoolTimeBoard {

    @Id
    @Column(name = "timeBoardId")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long timeBoardId ;

    @OneToOne
    @JoinColumn(name = "id")
    private Member member;

    private String firstMonTime;
    private String secondMonTime;
    private String thirdMontTime;
    private String fourthMonTime;
    private String fifthMontTime;
    private String sixthMonTime;
    private String seventhMonTime;
    private String eieghthMonTime;
    private String ninethMonTime;

    private String firstTueTime;
    private String secondTueTime;
    private String thirdTueTime;
    private String fourthTueTime;
    private String fifthTueTime;
    private String sixthTueTime;
    private String seventhTueTime;
    private String eieghthTueTime;
    private String ninethTueTime;

    private String firstWedTime;
    private String secondWedTime;
    private String thirdWedTime;
    private String fourthWedTime;
    private String fifthWedTime;
    private String sixthWedTime;
    private String seventhWedTime;
    private String eieghthWedTime;
    private String ninethWedTime;

    private String firstTurTime;
    private String secondTurTime;
    private String thirdTurTime;
    private String fourthTurTime;
    private String fifthTurTime;
    private String sixthTurTime;
    private String seventhTurTime;
    private String eieghthTurTime;
    private String ninethTurTime;

    private String firstFriTime;
    private String secondFriTime;
    private String thirdFriTime;
    private String fourthFriTime;
    private String fifthFriTime;
    private String sixthFriTime;
    private String seventhFriTime;
    private String eieghthFriTime;
    private String ninethFriTime;

    public SchoolTimeBoard newschoolTimeBoard() {
        SchoolTimeBoard schoolTimeBoard = new SchoolTimeBoard();
        schoolTimeBoard.setFirstMonTime("==");
        schoolTimeBoard.setSecondMonTime("==");
        schoolTimeBoard.setThirdMontTime("==");
        schoolTimeBoard.setFourthMonTime("==");
        schoolTimeBoard.setFifthMontTime("==");
        schoolTimeBoard.setSixthMonTime("==");
        schoolTimeBoard.setSeventhMonTime("==");
        schoolTimeBoard.setEieghthMonTime("==");
        schoolTimeBoard.setNinethMonTime("==");
        schoolTimeBoard.setFirstTueTime("==");
        schoolTimeBoard.setSecondTueTime("==");
        schoolTimeBoard.setThirdTueTime("==");
        schoolTimeBoard.setFourthTueTime("==");
        schoolTimeBoard.setFifthTueTime("==");
        schoolTimeBoard.setSixthTueTime("==");
        schoolTimeBoard.setSeventhTueTime("==");
        schoolTimeBoard.setEieghthTueTime("==");
        schoolTimeBoard.setNinethTueTime("==");
        schoolTimeBoard.setFirstWedTime("==");
        schoolTimeBoard.setSecondWedTime("==");
        schoolTimeBoard.setThirdWedTime("==");
        schoolTimeBoard.setFourthWedTime("==");
        schoolTimeBoard.setFifthWedTime("==");
        schoolTimeBoard.setSixthWedTime("==");
        schoolTimeBoard.setSeventhWedTime("==");
        schoolTimeBoard.setEieghthWedTime("==");
        schoolTimeBoard.setNinethWedTime("==");
        schoolTimeBoard.setFirstTurTime("==");
        schoolTimeBoard.setSecondTurTime("==");
        schoolTimeBoard.setThirdTurTime("==");
        schoolTimeBoard.setFourthTurTime("==");
        schoolTimeBoard.setFifthTurTime("==");
        schoolTimeBoard.setSixthTurTime("==");
        schoolTimeBoard.setSeventhTurTime("==");
        schoolTimeBoard.setEieghthTurTime("==");
        schoolTimeBoard.setNinethTurTime("==");
        schoolTimeBoard.setFirstFriTime("==");
        schoolTimeBoard.setSecondFriTime("==");
        schoolTimeBoard.setThirdFriTime("==");
        schoolTimeBoard.setFourthFriTime("==");
        schoolTimeBoard.setFifthFriTime("==");
        schoolTimeBoard.setSixthFriTime("==");
        schoolTimeBoard.setSeventhFriTime("==");
        schoolTimeBoard.setEieghthFriTime("==");
        schoolTimeBoard.setNinethFriTime("==");
        
        return schoolTimeBoard;
            }

    public SchoolTimeBoard() {

    }

    public void updateSchoolTimeBoard(SchoolTimeBoardDto dto) {
        this.firstMonTime = dto.getFirstMonTime();
        this.secondMonTime = dto.getSecondMonTime();
        this.thirdMontTime = dto.getThirdMontTime();
        this.fourthMonTime = dto.getFourthMonTime();
        this.fifthMontTime = dto.getFifthMontTime();
        this.sixthMonTime = dto.getSixthMonTime();
        this.seventhMonTime = dto.getSeventhMonTime();
        this.eieghthMonTime = dto.getEieghthMonTime();
        this.ninethMonTime = dto.getNinethMonTime();
        this.firstTueTime = dto.getFirstTueTime();
        this.secondTueTime = dto.getSecondTueTime();
        this.thirdTueTime = dto.getThirdTueTime();
        this.fourthTueTime = dto.getFourthTueTime();
        this.fifthTueTime = dto.getFifthTueTime();
        this.sixthTueTime = dto.getSixthTueTime();
        this.seventhTueTime = dto.getSeventhTueTime();
        this.eieghthTueTime = dto.getEieghthTueTime() ;
        this.ninethTueTime = dto.getNinethTueTime();
        this.firstWedTime = dto.getFirstWedTime();
        this.secondWedTime = dto.getSecondWedTime();
        this.thirdWedTime = dto.getThirdWedTime();
        this.fourthWedTime = dto.getFourthWedTime();
        this.fifthWedTime = dto.getFifthWedTime();
        this.sixthWedTime = dto.getSixthWedTime();
        this.seventhWedTime = dto.getSeventhWedTime();
        this.eieghthWedTime = dto.getEieghthWedTime();
        this.ninethWedTime = dto.getNinethWedTime();
        this.firstTurTime = dto.getFirstTurTime();
        this.secondTurTime = dto.getSecondTurTime();
        this.thirdTurTime = dto.getThirdTurTime();
        this.fourthTurTime = dto.getFourthTurTime();
        this.fifthTurTime = dto.getFifthTurTime();
        this.sixthTurTime = dto.getSixthTurTime();
        this.seventhTurTime = dto.getSeventhTurTime();
        this.eieghthTurTime = dto.getEieghthTurTime();
        this.ninethTurTime = dto.getNinethTurTime();
        this.firstFriTime = dto.getFirstFriTime();
        this.secondFriTime = dto.getSecondFriTime();
        this.thirdFriTime = dto.getThirdFriTime();
        this.fourthFriTime = dto.getFourthFriTime();
        this.fifthFriTime = dto.getFifthFriTime();
        this.sixthFriTime = dto.getSixthFriTime();
        this.seventhFriTime = dto.getSeventhFriTime();
        this.eieghthFriTime = dto.getEieghthFriTime();
        this.ninethFriTime = dto.getNinethFriTime();
        
    }
}
