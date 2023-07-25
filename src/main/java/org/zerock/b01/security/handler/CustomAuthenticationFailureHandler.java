package org.zerock.b01.security.handler;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {

        String msg = "아이디 또는 비밀번호가 맞지 않습니다.";
        String errorMsg = URLEncoder.encode(msg, "UTF-8");//"Invaild Username or Password";

        if(exception instanceof BadCredentialsException){

        }else if(exception instanceof InsufficientAuthenticationException){
            errorMsg = "Invalid Secret Key";
        }

        setDefaultFailureUrl("/member/login?error=true&exception="+errorMsg);

        super.onAuthenticationFailure(request,response,exception);
    }
}
