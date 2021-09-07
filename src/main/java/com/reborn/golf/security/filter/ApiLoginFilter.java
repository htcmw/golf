package com.reborn.golf.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.reborn.golf.entity.Member;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.security.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2

public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private JwtUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("=================ApiLoginFilter::attemptAuthentication==================");

        if(!request.getMethod().equals("POST")){
            throw new AuthenticationServiceException(" You Can use Only Post method");
        }
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("================successfulAuthentication====================");
        log.info("ssuccessfulAuthentication : " + authResult);
        log.info(authResult.getPrincipal());

        String email = ((AuthMemeberDto)authResult.getPrincipal()).getUsername();

        String token = null;
        try{
            token = jwtUtil.generateToken(email);
            response.setContentType("text/plain");
            response.getOutputStream().write(token.getBytes());

            log.info("token : " + token);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}

