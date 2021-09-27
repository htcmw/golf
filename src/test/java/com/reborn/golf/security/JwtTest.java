package com.reborn.golf.security;

import com.reborn.golf.security.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JwtTest {
    private JwtUtil jwtUtil;

    @BeforeEach
    public void testBefore(){
        System.out.println("================JwtTest ==================");
        jwtUtil = new JwtUtil();
    }

    @Test
    public void testEncode() throws Exception{
        String email = "user@naver.com";

//        String str = jwtUtil.generateToken(email);

//        System.out.println(str);
    }
}
