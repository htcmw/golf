package com.reborn.golf.security.service;

import com.reborn.golf.entity.Member;
import com.reborn.golf.repository.EmployeeRepository;
import com.reborn.golf.repository.MemberRepository;
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
    private final EmployeeRepository employeeRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        log.info("----------------loadUserByUsername, Member----------------------");

        Optional<Member> takenMember = memberRepository.findByEmail(username, false, false);

        if (takenMember.isPresent()) {
            Member member = takenMember.get();
            log.info(member);
            return new AuthMemeberDto(member.getIdx(), member.getEmail(), member.getPassword(), member.isFromSocial(),
                    member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority(memberRole.name())).collect(Collectors.toSet()));
        }

        throw new UsernameNotFoundException("Please Check Email or Social");
    }
}
