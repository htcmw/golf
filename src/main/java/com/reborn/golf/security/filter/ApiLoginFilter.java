package com.reborn.golf.security.filter;

import com.nimbusds.jose.util.IOUtils;
import com.reborn.golf.security.dto.AuthMemeberDto;
import com.reborn.golf.security.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;

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

        Reader body = request.getReader();
        StringBuilder sb = new StringBuilder();
        int intValueOfChar;
        while ((intValueOfChar = body.read()) != -1) {
            sb.append((char)intValueOfChar);
        }
        body.close();
        System.out.println(sb.toString());
        String email = null;
        String password = null;
        try{
            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(sb.toString());
            JSONObject jsonObject = (JSONObject) object;
            email = (String) jsonObject.get("email");
            password = (String) jsonObject.get("password");
        }catch (Exception e){
            e.printStackTrace();
        }

        log.info(email + ", " + password);

        log.info(email + ", " + password);
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

