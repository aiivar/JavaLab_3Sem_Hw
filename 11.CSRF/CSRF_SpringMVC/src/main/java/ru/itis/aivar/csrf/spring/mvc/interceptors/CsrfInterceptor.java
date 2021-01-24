package ru.itis.aivar.csrf.spring.mvc.interceptors;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Component
public class CsrfInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
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
                return true;
            } else {
                response.sendError(403);
                return false;
            }
        }
        return true;
    }
}
