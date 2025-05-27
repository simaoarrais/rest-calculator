package com.wit.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class RequestIdFilter implements Filter {

    private static final String REQUEST_ID = "requestId";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        try {
            String requestId = UUID.randomUUID().toString();
            MDC.put(REQUEST_ID, requestId);

            HttpServletRequest req = (HttpServletRequest) servletRequest;
            System.out.println("Received " + req.getMethod() + " " + req.getRequestURI() + " [requestId=" + requestId + "]");

            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            MDC.remove(REQUEST_ID); // Clean up after the request
        }
    }
}
