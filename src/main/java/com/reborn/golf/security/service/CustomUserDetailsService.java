package com.reborn.golf.security.service;

import com.reborn.golf.common.exception.NotExistEntityException;
import com.reborn.golf.member.entity.Member;
import com.reborn.golf.member.repository.MemberRepository;
import com.reborn.golf.security.dto.AuthMemeberDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(username, false, false)
                .orElseThrow(() -> new NotExistEntityException("IDX에 해당하는 고객정보가 DB에 없습니다"));
        return new AuthMemeberDto(member.getIdx(), member.getEmail(), member.getPassword(), member.isFromSocial(),
                member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(memberRole.name())).collect(Collectors.toSet()));
    }
}
