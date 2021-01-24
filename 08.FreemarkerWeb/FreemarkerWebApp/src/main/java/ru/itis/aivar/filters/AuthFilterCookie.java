package ru.itis.aivar.filters;

import ru.itis.aivar.models.User;
import ru.itis.aivar.services.CookieService;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthFilterCookie implements Filter {

    private CookieService cookieService;

    private final String[] protectedPaths = {
            "/profile-cookie"
    };

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        cookieService = (CookieService) servletContext.getAttribute("cookieService");
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
        Cookie[] cookies = request.getCookies();
        Cookie uuid = null;
        for (Cookie c: cookies){
            if (c.getName().equals("UUID")){
                uuid = c;
                break;
            }
        }
        if (uuid == null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        User user = cookieService.findByCookie(uuid);
        if (check && user==null){
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }
        request.setAttribute("user", user);
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
