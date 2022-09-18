package com.camper.entity;


import com.camper.constant.Role;
import com.camper.dto.MemberFormDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Table(name = "member")
@Getter
@Setter
@ToString
public class Member extends BaseTimeEntity {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String phoneNumber;

    /* 일반회원 or 호스트 */
    private String kind;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id")
    private Camp camp;


    /* passwordEncoder를 제외하고 MemberFormDto에서 값을 가져와서
    * Member 엔티티 테이블을 만드는 스태틱 메소드를 만들었습니다. */
    public static Member createMember(MemberFormDto memberFormDto,PasswordEncoder passwordEncoder){
        Member member = new Member();
        member.setName(memberFormDto.getName());
        member.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        member.setPassword(password);
        member.setPhoneNumber(memberFormDto.getPhoneNumber());
        member.setKind(memberFormDto.getKind());
        if(memberFormDto.getKind().equals("user")){
            member.setRole(Role.USER);
        }else if(memberFormDto.getKind().equals("host"))
            member.setRole(Role.HOST);

        return member;
    }

    public static Member updateMember(MemberFormDto memberFormDto,PasswordEncoder passwordEncoder,Member oldMember){
        oldMember.setName(memberFormDto.getName());
        oldMember.setEmail(memberFormDto.getEmail());
        String password = passwordEncoder.encode(memberFormDto.getPassword());
        oldMember.setPassword(password);
        oldMember.setPhoneNumber(memberFormDto.getPhoneNumber());
        oldMember.setKind(oldMember.getKind());
        oldMember.setRole(oldMember.getRole());
        return oldMember;
    }


    public static Member createAdmin(PasswordEncoder passwordEncoder){
        Member admin = new Member();
        admin.setName("관리자");
        admin.setEmail("admin@amorcamp.com");
        String password = passwordEncoder.encode("12341234");
        admin.setPassword(password);
        admin.setPhoneNumber("0");
        admin.setKind("Admin");
        admin.setRole(Role.ADMIN);

        return admin;
    }


}
