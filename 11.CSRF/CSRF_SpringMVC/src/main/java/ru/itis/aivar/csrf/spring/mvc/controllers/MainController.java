package ru.itis.aivar.csrf.spring.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import ru.itis.aivar.csrf.spring.mvc.model.User;
import ru.itis.aivar.csrf.spring.mvc.services.SecurityService;
import ru.itis.aivar.csrf.spring.mvc.utils.Validator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class MainController {

    @Autowired
    private SecurityService securityService;
    @Autowired
    private Validator validator;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginPage(){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(HttpServletRequest req){
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        if (securityService.singIn(req, email, password)){
            return "redirect:/profile";
        }
        req.setAttribute("email", req.getParameter("email"));
        return "redirect:/login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String getSignUpPage(){
        return "signup";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(HttpServletRequest req){
        String email = req.getParameter("email");
        String nickname = req.getParameter("username");
        String password = req.getParameter("password");
        String confPassword = req.getParameter("confPassword");
        if (validator.validatePassword(password) && validator.validateEmail(email) && validator.validateLogin(nickname) && password.equals(confPassword)) {
            if (securityService.signUp(req, email, nickname, password)) {
                return "redirect:/profile";
            } else {
                req.setAttribute("message", "User with this email exists");
                return "redirect:/signup";
            }
        } else {
            req.setAttribute("message", "Not valid data");
            return "redirect:/signup";
        }
    }

    @RequestMapping(value = "/profile")
    public String profile(HttpServletRequest req){
        User user = (User) req.getSession().getAttribute("user");
        req.setAttribute("username", user.getNickname());
        req.setAttribute("email", user.getEmail());
        return "profile";
    }

    @RequestMapping(value = "/")
    public String redirectToLogin(){
        return "redirect:/login";
    }

}
