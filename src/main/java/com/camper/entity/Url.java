package com.camper.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "url")
@Getter
@Setter
@ToString
public class Url {
    @Id
    @Column(name = "url_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String Url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public static Url createUrl(String Url,Member member){
        Url url = new Url();
        url.setUrl(Url);
        url.setMember(member);
        return url;
    }

    public void updateUrl(String url){
        this.Url = url;
    }

}