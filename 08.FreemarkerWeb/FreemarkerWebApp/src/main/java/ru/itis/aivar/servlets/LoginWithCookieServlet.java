package ru.itis.aivar.servlets;

import ru.itis.aivar.models.User;
import ru.itis.aivar.services.CookieService;
import ru.itis.aivar.services.SecurityService;
import ru.itis.aivar.services.UsersService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@WebServlet("/login-cookie")
public class LoginWithCookieServlet extends HttpServlet {
    private CookieService cookieService;
    private UsersService usersService;
    private SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        this.cookieService = (CookieService) servletContext.getAttribute("cookieService");
        this.usersService = (UsersService) servletContext.getAttribute("usersService");
        this.securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/views/login-cookie.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (securityService.singIn(req, email, password)){
            User user = usersService.findByEmail(email);
            Cookie cookie = new Cookie("UUID", UUID.randomUUID().toString());
            resp.addCookie(cookie);
            cookieService.save(cookie, user);
            resp.sendRedirect(getServletContext().getContextPath()+"/profile-cookie");
            return;
        }
        req.setAttribute("email", req.getParameter("email"));
        getServletContext().getRequestDispatcher("/WEB-INF/view/login-cookie.ftlh").forward(req, resp);
    }
}
