package com.mipt.mikhaildubov.todo.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class AccessLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        String traceId = MDC.get("traceId");

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            int status = response.getStatus();
            String method = request.getMethod();
            String path = request.getRequestURI();
            String authHeader = request.getHeader("Authorization");
            String maskedAuth = maskAuthHeader(authHeader);

            log.info("HTTP {} {} -> status={} timeMs={} trace={} auth={}",
                    method, path, status, duration, traceId, maskedAuth);
        }
    }

    private String maskAuthHeader(String authHeader) {
        if (!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")) {
            return "none";
        }
        String token = authHeader.substring(7);
        if (token.length() <= 12) {
            return "Bearer ***";
        }
        return "Bearer " + token.substring(0, 6) + "***" + token.substring(token.length() - 6);
    }
}