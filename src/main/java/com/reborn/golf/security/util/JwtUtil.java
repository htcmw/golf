package com.reborn.golf.security.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Date;

@Log4j2
public class JwtUtil {
    private String secretKey = "golf12345678";

    //토큰 유효시간 1 month
    private long expire = 60 * 24 * 30;

    public String generateToken(String email, Collection<GrantedAuthority> roleSet) throws Exception{
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("email",email)
                .claim("role",roleSet)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes("UTF-8"))
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception{
        String contentValue = null;
        try{
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes("UTF-8")).parseClaimsJws(tokenStr);

            log.info(defaultJws);

            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            contentValue = claims.getSubject();
            log.info(contentValue);

        }catch (Exception e){
            log.error(e.getMessage());
            contentValue = null;
        }
        return contentValue;
    }
}
