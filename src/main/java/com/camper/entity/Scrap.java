package com.camper.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "scrap")
@Getter
@Setter
@ToString
public class Scrap {

    @Id
    @Column(name = "scrap_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Scrap createScrap(Member member){
        Scrap scrap = new Scrap();
        scrap.setMember(member);
        return scrap;
    }

}
