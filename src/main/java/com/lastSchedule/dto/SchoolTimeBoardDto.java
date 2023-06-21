package com.lastSchedule.dto;

import com.lastSchedule.entity.Member;
import com.lastSchedule.entity.SchoolTimeBoard;
import com.lastSchedule.entity.SuggestBoard;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.modelmapper.ModelMapper;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


@Getter@Setter
@ToString
public class SchoolTimeBoardDto {

    private Long timeBoardId ;

    @NotEmpty
    private String firstMonTime;

        @NotEmpty
    private String secondMonTime;

    @NotEmpty
    private String thirdMontTime;

    @NotEmpty
    private String fourthMonTime;
    @NotEmpty
    private String fifthMontTime;
    @NotEmpty
    private String sixthMonTime;
    @NotEmpty
    private String seventhMonTime;
    @NotEmpty
    private String eieghthMonTime;
    @NotEmpty
    private String ninethMonTime;
    
    
    @NotEmpty
    private String firstTueTime;
    @NotEmpty
    private String secondTueTime;
    @NotEmpty
    private String thirdTueTime;
    @NotEmpty
     private String fourthTueTime;
    @NotEmpty
    private String fifthTueTime;
    @NotEmpty
    private String sixthTueTime;
    @NotEmpty
    private String seventhTueTime;
        @NotEmpty
    private String eieghthTueTime;
        @NotEmpty
    private String ninethTueTime;

        
    @NotEmpty
    private String firstWedTime;
        @NotEmpty
    private String secondWedTime;
        @NotEmpty
    private String thirdWedTime;
        @NotEmpty
    private String fourthWedTime;
        @NotEmpty
    private String fifthWedTime;
        @NotEmpty
    private String sixthWedTime;
        @NotEmpty
    private String seventhWedTime;
        @NotEmpty
    private String eieghthWedTime;
        @NotEmpty
    private String ninethWedTime;

        
        @NotEmpty
    private String firstTurTime;
        @NotEmpty
    private String secondTurTime;
        @NotEmpty
    private String thirdTurTime;
        @NotEmpty
    private String fourthTurTime;
        @NotEmpty
    private String fifthTurTime;
        @NotEmpty
    private String sixthTurTime;
        @NotEmpty
    private String seventhTurTime;
        @NotEmpty
    private String eieghthTurTime;
        @NotEmpty
    private String ninethTurTime;

        @NotEmpty
    private String firstFriTime;
        @NotEmpty
    private String secondFriTime;
        @NotEmpty
    private String thirdFriTime;
        @NotEmpty
    private String fourthFriTime;
        @NotEmpty
    private String fifthFriTime;
        @NotEmpty
    private String sixthFriTime;
        @NotEmpty
    private String seventhFriTime;
        @NotEmpty
    private String eieghthFriTime;
        @NotEmpty
    private String ninethFriTime;


    public SchoolTimeBoardDto() {
        this.firstMonTime = "==";
        this.secondMonTime = "==";
        this.thirdMontTime = "==";
        this.fourthMonTime = "==";
        this.fifthMontTime = "==";
        this.sixthMonTime = "==";
        this.seventhMonTime = "==";
        this.eieghthMonTime = "==";
        this.ninethMonTime = "==";
        this.firstTueTime = "==";
        this.secondTueTime = "==";
        this.thirdTueTime = "==";
        this.fourthTueTime = "==";
        this.fifthTueTime = "==";
        this.sixthTueTime = "==";
        this.seventhTueTime = "==";
        this.eieghthTueTime = "==";
        this.ninethTueTime = "==";
        this.firstWedTime = "==";
        this.secondWedTime = "==";
        this.thirdWedTime = "==";
        this.fourthWedTime = "==";
        this.fifthWedTime = "==";
        this.sixthWedTime = "==";
        this.seventhWedTime = "==";
        this.eieghthWedTime = "==";
        this.ninethWedTime = "==";
        this.firstTurTime = "==";
        this.secondTurTime = "==";
        this.thirdTurTime = "==";
        this.fourthTurTime = "==";
        this.fifthTurTime = "==";
        this.sixthTurTime = "==";
        this.seventhTurTime = "==";
        this.eieghthTurTime = "==";
        this.ninethTurTime = "==";
        this.firstFriTime = "==";
        this.secondFriTime = "==";
        this.thirdFriTime = "==";
        this.fourthFriTime = "==";
        this.fifthFriTime = "==";
        this.sixthFriTime = "==";
        this.seventhFriTime = "==";
        this.eieghthFriTime = "==";
        this.ninethFriTime = "==";
    }

    private static ModelMapper modelMapper = new ModelMapper();

    public static SchoolTimeBoardDto of(SchoolTimeBoard schoolTimeBoard){
        System.out.println("SuggestBoardFormDto - 0f");
        return modelMapper.map(schoolTimeBoard, SchoolTimeBoardDto.class);
    }

    public SchoolTimeBoard updateSchoolTimeBoard() {
        return modelMapper.map(this, SchoolTimeBoard.class);
    }
}
