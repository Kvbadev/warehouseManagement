package com.kvbadev.wms.presentation.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

public class FilterChainExceptionHandler extends OncePerRequestFilter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final HandlerExceptionResolver resolver;

    public FilterChainExceptionHandler(HandlerExceptionResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            logger.error("Spring Security Filter Chain Exception:", e);
            ModelAndView isHandled = resolver.resolveException(request,response,null,e);
            if(isHandled == null) {
                throw e;
            }
        }
    }
}