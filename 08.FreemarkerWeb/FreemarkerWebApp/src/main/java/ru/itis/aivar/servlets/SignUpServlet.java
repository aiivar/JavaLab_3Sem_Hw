package ru.itis.aivar.servlets;

import ru.itis.aivar.services.SecurityService;
import ru.itis.aivar.services.UsersService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static ru.itis.aivar.utils.Validator.*;

@WebServlet("/signup")
public class SignUpServlet extends HttpServlet {
    SecurityService securityService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ServletContext servletContext = config.getServletContext();
        securityService = (SecurityService) servletContext.getAttribute("securityService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        getServletContext().getRequestDispatcher("/WEB-INF/views/signup.ftlh").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String nickname = req.getParameter("username");
        String password = req.getParameter("password");
        String confPassword = req.getParameter("confPassword");
        if (validatePassword(password) && validateEmail(email) && validateLogin(nickname) && password.equals(confPassword)) {
            if (securityService.signUp(req, email, nickname, password)) {
                resp.sendRedirect(getServletContext().getContextPath() + "/profile");
            } else {
                req.setAttribute("message", "User with this email exists");
                getServletContext().getRequestDispatcher("/WEB-INF/view/signup.ftlh").forward(req, resp);
            }
        } else {
            req.setAttribute("message", "Not valid data");
            getServletContext().getRequestDispatcher("/WEB-INF/views/signup.ftlh").forward(req, resp);
        }

    }
}
