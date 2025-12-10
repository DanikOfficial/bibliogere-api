package com.pete.bibliogere.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;

@Component
@Log4j2
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // Wrap request and response to cache content
        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        long startTime = System.currentTimeMillis();

        // Log request
        logRequest(requestWrapper);

        // Continue with the filter chain
        filterChain.doFilter(requestWrapper, responseWrapper);

        long duration = System.currentTimeMillis() - startTime;

        // Log response
        logResponse(responseWrapper, duration);

        // Important: Copy cached response content to actual response
        responseWrapper.copyBodyToResponse();
    }

    private void logRequest(ContentCachingRequestWrapper request) {
        StringBuilder requestLog = new StringBuilder();
        requestLog.append("\n========== INCOMING REQUEST ==========\n");
        requestLog.append("Method: ").append(request.getMethod()).append("\n");
        requestLog.append("URI: ").append(request.getRequestURI()).append("\n");

        String queryString = request.getQueryString();
        if (queryString != null) {
            requestLog.append("Query String: ").append(queryString).append("\n");
        }

        // Log headers
        requestLog.append("Headers: \n");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String headerValue = request.getHeader(headerName);
            // Mask sensitive headers
            if (headerName.equalsIgnoreCase("Authorization")) {
                headerValue = "Bearer [MASKED]";
            }
            requestLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        }

        // Log request body
        String requestBody = getContentAsString(request.getContentAsByteArray(), request.getCharacterEncoding());
        if (!requestBody.isEmpty()) {
            requestLog.append("Body: ").append(requestBody).append("\n");
        }

        requestLog.append("=====================================");
        log.info(requestLog.toString());
    }

    private void logResponse(ContentCachingResponseWrapper response, long duration) {
        StringBuilder responseLog = new StringBuilder();
        responseLog.append("\n========== OUTGOING RESPONSE ==========\n");
        responseLog.append("Status: ").append(response.getStatus()).append("\n");
        responseLog.append("Duration: ").append(duration).append(" ms\n");

        // Log response headers
        responseLog.append("Headers: \n");
        response.getHeaderNames().forEach(headerName -> {
            String headerValue = response.getHeader(headerName);
            responseLog.append("  ").append(headerName).append(": ").append(headerValue).append("\n");
        });

        // Log response body
        String responseBody = getContentAsString(response.getContentAsByteArray(), response.getCharacterEncoding());
        if (!responseBody.isEmpty()) {
            responseLog.append("Body: ").append(responseBody).append("\n");
        }

        responseLog.append("======================================");
        log.info(responseLog.toString());
    }

    private String getContentAsString(byte[] content, String encoding) {
        if (content == null || content.length == 0) {
            return "";
        }
        try {
            return new String(content, encoding != null ? encoding : "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error("Error decoding content", e);
            return "[Error decoding content]";
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Skip logging for static resources, actuator, and swagger
        return path.startsWith("/actuator") ||
                path.startsWith("/swagger-ui") ||
                path.startsWith("/v3/api-docs") ||
                path.startsWith("/webjars");
    }
}
