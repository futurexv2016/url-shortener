package com.bondle.shortenurl.api;

import org.springframework.web.filter.*;
import org.springframework.web.util.*;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/**
 * Filter to log all requests and responses app returns to client for debugging and tracing
 * @author david.ho
 */
public class APILoggingFilter extends OncePerRequestFilter {
    private final int maxPayloadLength = 1000;

    private String getContentAsString(byte[] buf, int maxLength, String charsetName) {
        if (buf == null || buf.length == 0) return "";
        int length = Math.min(buf.length, this.maxPayloadLength);
        try {
            return new String(buf, 0, length, charsetName);
        } catch (UnsupportedEncodingException ex) {
            return "Unsupported Encoding";
        }
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        long startTime = System.currentTimeMillis();
        StringBuilder reqInfo = new StringBuilder()
            .append("[")
            .append(startTime % 10000)
            .append("] ")
            .append(request.getMethod())
            .append(" ")
            .append(request.getRequestURL());

        String queryString = request.getQueryString();
        if (queryString != null) {
            reqInfo.append("?").append(queryString);
        }

        if (request.getAuthType() != null) {
            reqInfo.append(", authType=")
                .append(request.getAuthType());
        }
        if (request.getUserPrincipal() != null) {
            reqInfo.append(", principalName=")
                .append(request.getUserPrincipal().getName());
        }

        ContentCachingRequestWrapper wrappedRequest = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper wrappedResponse = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(wrappedRequest, wrappedResponse);
        long duration = System.currentTimeMillis() - startTime;

        String requestBody = this.getContentAsString(wrappedRequest.getContentAsByteArray(), this.maxPayloadLength, request.getCharacterEncoding());
        StringBuilder requestStr = new StringBuilder(reqInfo);

        if (requestBody.length() > 0) {
            requestStr.append(",   Request body: ").append(requestBody);
        }

        this.logger.debug("Request => " + requestStr);

        byte[] buf = wrappedResponse.getContentAsByteArray();
        StringBuilder responseStr = new StringBuilder("Response <= ");
        responseStr
            .append(reqInfo)
            .append(" in ")
            .append(duration)
            .append("ms")
            .append(": returned status=")
            .append(response.getStatus())
            .append(",   Response body: ")
            .append(getContentAsString(buf, this.maxPayloadLength, response.getCharacterEncoding()));
        this.logger.debug(responseStr);

        wrappedResponse.copyBodyToResponse();
    }
}
