package com.example.demo.utils;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;


@Component
@Order(1)
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String tenantName = request.getHeader("tenantId");
        TenantContext.setCurrentTenant(tenantName);

        try {
          filterChain.doFilter(servletRequest,servletResponse);
        } finally {
          TenantContext.setCurrentTenant("");
        }
    }
}
