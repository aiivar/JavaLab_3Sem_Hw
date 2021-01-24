package ru.itis.aivar.services;

//import ru.itis.aivar.models.Title;
import ru.itis.aivar.models.User;

import java.util.List;

public interface UsersService {
    List<User> getAllUsers();
    boolean save(User user);
    User findByEmail(String email);
    User findById(Long id);
    boolean update(User user);
//    void addTitle(User user, Title title);
}
