package ru.itis.aivar.filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CsrfFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        if (request.getMethod().equals("GET")){
            String csrf = UUID.randomUUID().toString();
            request.setAttribute("_csrf_token", csrf);
            Set<String> csrfSet = (Set<String>) request.getSession().getAttribute("_csrf_tokens");
            if (csrfSet == null){
                csrfSet = new HashSet<>();
            }
            csrfSet.add(csrf);
            request.getSession().setAttribute("_csrf_tokens", csrfSet);
        }
        if (request.getMethod().equals("POST")){
            Set<String> sessCsrf = (Set<String>) request.getSession(false).getAttribute("_csrf_tokens");
            String reqCsrf = request.getParameter("_csrf_token");
            if (sessCsrf.contains(reqCsrf)){
                sessCsrf.remove(reqCsrf);
                filterChain.doFilter(request, response);
                return;
            } else {
                response.sendError(403);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
