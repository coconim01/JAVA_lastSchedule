package com.lastSchedule.config;

import com.lastSchedule.entity.Member;
import com.lastSchedule.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        WebAuthenticationDetails authDetails = (WebAuthenticationDetails) authentication.getDetails();
        String email = authentication.getName();

        Member member = memberRepository.findByEmail(email);
        if (member != null) {
            HttpSession session = request.getSession();
            session.setAttribute("username", email);
            session.setAttribute("name", member.getName());
        }

        super.onAuthenticationSuccess(request, response, authentication);
    }
}
