package com.camper.entity;

import com.camper.dto.CampFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "camp")
@Getter
@Setter
@ToString
public class Camp extends BaseTimeEntity {

    @Id
    @Column(name = "camp_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String campName;

    private String address;

    private String bNumber;

    private String campArea;

    private String surroundings;

    private String inTime;

    private String outTime;

    private String campInfo;

    private String permission;

    private int ratingAvg;

    private int count;


    public void updatePermission(String permission){
        this.permission = permission;
    }

    public void addCount(){
        ++count;
    }
    public void updateCamp(CampFormDto campFormDto){
        this.campName = campFormDto.getCampName();
        this.address = campFormDto.getAddress();
        this.bNumber = campFormDto.getBNumber();
        this.campArea = campFormDto.getCampArea();
        this.surroundings = campFormDto.getSurroundings();
        this.inTime = campFormDto.getInTime();
        this.outTime = campFormDto.getOutTime();
        this.campInfo = campFormDto.getCampInfo();
    }

}
