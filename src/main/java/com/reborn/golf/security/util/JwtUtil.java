package com.reborn.golf.security.util;

import com.reborn.golf.security.dto.AuthMemeberDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Log4j2
public class JwtUtil {
    private final String secretKey = "golf12345678";

    //토큰 유효시간 1 month
    private final long expire = 60 * 24 * 30;

    public String generateToken(Authentication authentication) throws Exception {

        Integer idx = ((AuthMemeberDto) authentication.getPrincipal()).getIdx();
        String email = authentication.getName();
        String authorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        Boolean fromSocial = ((AuthMemeberDto) authentication.getPrincipal()).isFromSocial();

        log.info(idx + email + authorities);

        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("id", idx)
                .claim("role", authorities)
                .claim("social", fromSocial)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public Claims validateAndExtract(String tokenStr) throws Exception {
        return Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr).getBody();
    }
}
