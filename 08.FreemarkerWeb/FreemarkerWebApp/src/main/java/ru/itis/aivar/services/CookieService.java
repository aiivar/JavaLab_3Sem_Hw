package ru.itis.aivar.services;

import ru.itis.aivar.models.User;

import javax.servlet.http.Cookie;

public interface CookieService {

    User findByCookie(Cookie cookie);

    void save(Cookie cookie, User user);

}
