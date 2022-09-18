package com.camper.entity;

import com.camper.constant.RoomStatus;
import com.camper.dto.RoomFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "room")
@Getter
@Setter
@ToString
public class Room {
    @Id
    @Column(name = "room_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String roomName;

    private Integer roomPrice;

    private String roomShortInfo;

    private String roomDetailInfo;

    private String campType;


    @Enumerated(EnumType.STRING)
    private RoomStatus roomStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    public void updateRoom(RoomFormDto roomFormDto){
        this.roomName = roomFormDto.getRoomName();
        this.roomPrice = roomFormDto.getRoomPrice();
        this.roomShortInfo = roomFormDto.getRoomShortInfo();
        this.roomDetailInfo = roomFormDto.getRoomDetailInfo();
        this.campType = roomFormDto.getCampType();
    }
}
