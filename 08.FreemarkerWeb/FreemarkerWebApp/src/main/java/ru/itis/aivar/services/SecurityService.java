package ru.itis.aivar.services;

import javax.servlet.http.HttpServletRequest;

public interface SecurityService {
    boolean isSigned(HttpServletRequest req);
    boolean singIn(HttpServletRequest req, String email, String password);
    boolean signUp(HttpServletRequest req, String email, String nickname , String password);
    void signOut(HttpServletRequest req);
    String encode(String password);
    boolean matches(String rawPassword, String hashPassword);
}
