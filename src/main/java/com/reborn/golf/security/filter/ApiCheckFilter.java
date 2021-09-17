package com.reborn.golf.security.filter;

import com.reborn.golf.entity.Member;
import com.reborn.golf.repository.MemberRepository;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.security.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;


    public ApiCheckFilter(JwtUtil jwtUtil, MemberRepository memberRepository) {
        this.jwtUtil = jwtUtil;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("=================ApiCheckFilter==================");
        log.info("Request URL : " + request.getRequestURI());


        Authentication authentication = getUsernamePasswordAuthenticationToken(request);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthenticationToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist : " + authHeader);

            try {
                String email = jwtUtil.validateAndExtract(authHeader.substring(7));

                log.info("validate result : " + email);

                Optional<Member> result = memberRepository.findByEmail(email,false);

                if (result.isEmpty())
                    throw new UsernameNotFoundException("Please Check Email or Social");

                Member member = result.get();

                log.info("----------------loadUserByUsername----------------------");
                log.info("member : " + member);

                AuthMemeberDto authMemeberDto = new AuthMemeberDto(member.getEmail(), "", false,  member.getRoleSet().stream().map(memberRole -> new SimpleGrantedAuthority("ROLE_" + memberRole.name())).collect(Collectors.toSet()));
                authMemeberDto.setName(member.getName());
                return new UsernamePasswordAuthenticationToken(authMemeberDto, null, authMemeberDto.getAuthorities());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}