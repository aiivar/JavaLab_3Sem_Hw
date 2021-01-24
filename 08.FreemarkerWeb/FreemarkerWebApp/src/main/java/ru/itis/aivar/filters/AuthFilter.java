package ru.itis.aivar.filters;

import ru.itis.aivar.services.SecurityService;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebFilter("/*")
public class AuthFilter implements Filter {

    private final String[] protectedPaths = {
            "/profile"
    };

    private SecurityService securityService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String path = request.getRequestURI().substring(request.getContextPath().length());
        boolean check = false;
        for (String p: protectedPaths){
            if (path.equals(p)){
                check = true;
                break;
            }
        }
        if (check && !securityService.isSigned(request)){
            response.sendRedirect(request.getContextPath()+"/login");
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
