package com.reborn.golf.security.filter;

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
import java.util.HashMap;

@Log4j2

public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {

    private final JwtUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JwtUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("=================ApiLoginFilter::attemptAuthentication==================");

        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException(" You can use only Post method");
        }

        HashMap<String, String> member = extractEmailAndPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(member.get("email"), member.get("password"));

        return getAuthenticationManager().authenticate(authToken);
    }

    private HashMap<String, String> extractEmailAndPassword(HttpServletRequest request) {
        log.info("================extractEmailAndPassword====================");
        HashMap<String, String> member = new HashMap<>();

        try {
            StringBuilder sb = new StringBuilder();
            Reader body = request.getReader();
            int intValueOfChar;
            while ((intValueOfChar = body.read()) != -1) {
                sb.append((char) intValueOfChar);
            }
            body.close();


            JSONParser jsonParser = new JSONParser();
            Object object = jsonParser.parse(sb.toString());
            JSONObject jsonObject = (JSONObject) object;

            member.put("email", (String) jsonObject.get("email"));
            member.put("password", (String) jsonObject.get("password"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return member;
    }
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("================successfulAuthentication====================");

        try {
            String token = jwtUtil.generateToken(authentication);
            response.setContentType("application/json");
            response.getOutputStream().write(token.getBytes());

            log.info("token : " + token);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

