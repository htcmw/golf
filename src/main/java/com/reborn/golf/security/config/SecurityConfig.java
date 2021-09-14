package com.reborn.golf.security.config;

import com.reborn.golf.repository.MemberRepository;
import com.reborn.golf.security.filter.ApiCheckFilter;
import com.reborn.golf.security.filter.ApiLoginFilter;
import com.reborn.golf.security.handler.ApiLoginFailHandler;
import com.reborn.golf.security.service.CustomUserDetailsService;
import com.reborn.golf.security.util.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Log4j2
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true,  securedEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberRepository memberRepository;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    @Bean
    public ApiCheckFilter apiCheckFilter(){
        return new ApiCheckFilter(jwtUtil(), memberRepository);
    }

    @Bean
    public ApiLoginFilter apiLoginFilter() throws Exception{
        ApiLoginFilter apiLoginFilter =  new ApiLoginFilter("/member/login",jwtUtil());
        apiLoginFilter.setAuthenticationManager(authenticationManager());
        apiLoginFilter.setAuthenticationFailureHandler(new ApiLoginFailHandler());
        return apiLoginFilter;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.formLogin().disable()
                .csrf().disable()
                .logout().disable();
        http.headers().httpStrictTransportSecurity()
                .maxAgeInSeconds(0)
                .includeSubDomains(true);

        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(apiCheckFilter(), UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(apiLoginFilter(), UsernamePasswordAuthenticationFilter.class);

    }
}