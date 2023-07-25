package org.zerock.b01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.zerock.b01.security.CustomUserDetailsService;
import org.zerock.b01.security.handler.Custom403Handler;
import org.zerock.b01.security.handler.CustomSocialLoginSuccessHandler;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    private final DataSource dataSource;
    private final CustomUserDetailsService userDetailsService;
    private final AuthenticationFailureHandler customFailureHandler;

    //passwordEncoder 주입
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //로그인을 하지 않아도 board/list에 바로 접근이 가능하다.
    //모든 사용자가 모든 경로에 접근할 수 있는 메서드 -> 최소한의 설정으로 필요한 자원의 접근을 제어할 예정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("------------configuire-----------");
        http    .csrf().disable() // CSRF 토큰 비활성화
                .formLogin().loginPage("/member/login").defaultSuccessUrl("/board/list")  //login 화면에서 로그인을 진행한다는 설정
                .failureHandler(customFailureHandler)
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/board/list");

        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailsService)
                .tokenValiditySeconds(60*60*24*30);

        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); //403
        http.oauth2Login()
                .loginPage("/member/login")
                .successHandler(authenticationSuccessHandler());

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new Custom403Handler();
    }

    //로그인 성공 처리 시 이용
    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

    /**
     * 정적 자원의 처리 (css, js)(정적으로 동작하는 파일들) -> 시큐리티 적용할 필요가 없음
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        log.info("----------web configure-------------");

        WebSecurityCustomizer webSecurityCustomizer = (web) -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
        return webSecurityCustomizer;
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }
}
