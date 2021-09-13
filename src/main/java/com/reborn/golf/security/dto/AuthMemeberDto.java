package com.reborn.golf.security.dto;

import com.reborn.golf.entity.MemberRole;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Log4j2
@Getter
@Setter
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
