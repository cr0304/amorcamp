package com.camper.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "scrap_camp")
public class ScrapCamp extends BaseTimeEntity {

    @Id
    @GeneratedValue
    @Column(name="scrap_camp_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scrap_id")
    private Scrap scrap;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;

    public static ScrapCamp createScrapCamp(Scrap scrap, Camp camp){
        ScrapCamp scrapCamp = new ScrapCamp();
        scrapCamp.setScrap(scrap);
        scrapCamp.setCamp(camp);
        return scrapCamp;
    }

}
