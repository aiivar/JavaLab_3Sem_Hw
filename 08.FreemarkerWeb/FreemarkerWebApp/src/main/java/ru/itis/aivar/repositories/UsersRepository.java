package ru.itis.aivar.repositories;

//import ru.itis.aivar.models.Title;
import ru.itis.aivar.models.User;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByEmail(String email);
//    void addTitle(User user, Title title);

}
