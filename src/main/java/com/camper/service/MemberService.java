package com.camper.service;

import com.camper.dto.MemberFormDto;
import com.camper.entity.Member;
import com.camper.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor //final이나 @NonNull이 붙은 필드(변수) 생성자를 생성해줍니다.
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;

    public Member saveMember(Member member){
        validateDuplicateMember(member);
        return memberRepository.save(member);
    }

    public void updateMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder, Member oldMember){
        Member newMember = Member.updateMember(memberFormDto, passwordEncoder,oldMember);
        memberRepository.save(newMember);
    }


    private void validateDuplicateMember(Member member){
        Member findMember = memberRepository.findByEmail(member.getEmail());
        if(findMember != null){
            throw new IllegalStateException("이미 가입된 회원입니다.");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);

        if(member == null){
            throw new UsernameNotFoundException(email);
        }

        return User.builder().username(member.getEmail())
                .password(member.getPassword())
                .roles(member.getRole().toString())
                .build();
    }

    public MemberFormDto getMemberInfo(String email) throws UsernameNotFoundException{
        Member member = memberRepository.findByEmail(email);

        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName(member.getName()); //유저 이름
        memberFormDto.setEmail(member.getEmail()); //유저 이메일
        memberFormDto.setPassword(member.getPassword()); //유저 비밀번호
        memberFormDto.setPhoneNumber(member.getPhoneNumber()); //유저 핸드폰 번호

        return memberFormDto;

    }

    public Member findMember(String email){
        return memberRepository.findByEmail(email);
    }

}
