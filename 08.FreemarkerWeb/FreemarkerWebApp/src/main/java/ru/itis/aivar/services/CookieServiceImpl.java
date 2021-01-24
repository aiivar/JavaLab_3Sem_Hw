package ru.itis.aivar.services;

import ru.itis.aivar.models.CookieMock;
import ru.itis.aivar.models.User;
import ru.itis.aivar.repositories.CookiesRepository;
import ru.itis.aivar.repositories.UsersRepository;

import javax.servlet.http.Cookie;

public class CookieServiceImpl implements CookieService{
    private CookiesRepository cookiesRepository;
    private UsersRepository usersRepository;

    public CookieServiceImpl(CookiesRepository cookiesRepository, UsersRepository usersRepository) {
        this.cookiesRepository = cookiesRepository;
        this.usersRepository = usersRepository;
    }

    @Override
    public User findByCookie(Cookie cookie) {
        String uuid = cookie.getValue();
        CookieMock cookieMock = cookiesRepository.findByUUID(uuid);
        if (cookieMock == null){
            return null;
        }
        return usersRepository.findById(cookieMock.getUserId()).orElse(null);
    }

    @Override
    public void save(Cookie cookie, User user) {
        String uuid = cookie.getValue();
        Long id = user.getId();
        CookieMock cookieMock = CookieMock.builder()
                .userId(id)
                .uuid(uuid)
                .build();
        cookiesRepository.save(cookieMock);
    }
}
