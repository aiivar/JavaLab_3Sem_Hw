package ru.itis.aivar.csrf.spring.mvc.repositories;



import ru.itis.aivar.csrf.spring.mvc.model.User;

import java.util.Optional;

public interface UsersRepository extends CrudRepository<User> {

    Optional<User> findByEmail(String email);

}
