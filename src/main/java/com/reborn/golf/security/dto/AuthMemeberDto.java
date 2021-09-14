package com.reborn.golf.security.dto;

import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Log4j2
@Setter
@Getter
@ToString
public class AuthMemeberDto extends User {

    private String username;
    private String name;
    private boolean fromSocial;

    public AuthMemeberDto(String username, String password, boolean fromSocial, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.username = username;
        this.fromSocial = fromSocial;
    }

}
