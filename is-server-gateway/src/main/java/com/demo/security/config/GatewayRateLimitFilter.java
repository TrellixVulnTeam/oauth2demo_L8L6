package com.demo.security.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GatewayRateLimitFilter extends OncePerRequestFilter {

    private RateLimiter rateLimiter = RateLimiter.create(1);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("0 rate limit ");

        if(rateLimiter.tryAcquire()) {
            filterChain.doFilter(request, response);
        }else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"too many request\"}");
            response.getWriter().flush();
            return;
        }
    }

}
