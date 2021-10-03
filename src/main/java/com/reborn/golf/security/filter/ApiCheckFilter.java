package com.reborn.golf.security.filter;

import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

@Log4j2
public class ApiCheckFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;


    public ApiCheckFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("=================ApiCheckFilter==================");


        Authentication authentication = getUsernamePasswordAuthenticationToken(request);

        if (authentication != null) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        log.info("Request URL : " + request.getRequestURI());

        filterChain.doFilter(request, response);
    }

    private Authentication getUsernamePasswordAuthenticationToken(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            log.info("Authorization exist : " + authHeader);

            try {
                Claims claims = jwtUtil.validateAndExtract(authHeader.substring(7));

                String email = claims.getSubject();
                Integer idx = claims.get("id",Integer.class);
                Boolean fromSocial = claims.get("social",Boolean.class);
                String[] authorities = claims.get("role",String.class).split(",");

                AuthMemeberDto authMemeberDto = new AuthMemeberDto(idx,email, "", fromSocial, Arrays.stream(authorities).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));

                return new UsernamePasswordAuthenticationToken(authMemeberDto, null, authMemeberDto.getAuthorities());

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return null;
    }
}