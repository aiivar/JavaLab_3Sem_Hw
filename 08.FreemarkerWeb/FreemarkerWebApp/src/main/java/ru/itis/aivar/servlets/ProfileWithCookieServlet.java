package ru.itis.aivar.servlets;

import ru.itis.aivar.models.User;
import ru.itis.aivar.services.CookieService;
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

@WebServlet("/profile-cookie")
public class ProfileWithCookieServlet extends HttpServlet {
    private CookieService cookieService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        cookieService = (CookieService) servletContext.getAttribute("cookieService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cookie[] cookies = req.getCookies();
        Cookie uuid = null;
        for (Cookie c: cookies){
            if (c.getName().equals("UUID")){
                uuid = c;
                break;
            }
        }
        User user = cookieService.findByCookie(uuid);
        if (user == null){
            resp.sendRedirect(req.getContextPath()+"/login-cookie");
            return;
        }
        req.setAttribute("username", user.getNickname());
        req.setAttribute("email", user.getEmail());
        getServletContext().getRequestDispatcher("/WEB-INF/views/profile.ftlh").forward(req, resp);
    }

}
