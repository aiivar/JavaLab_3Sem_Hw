package ru.itis.aivar.csrf.spring.mvc.repositories;



import ru.itis.aivar.csrf.spring.mvc.model.User;
import ru.itis.aivar.csrf.spring.mvc.repositories.exceptions.db.NoneUniqueValueException;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class UsersRepositoryJdbcImpl implements UsersRepository {

    //language=SQL
    private static final String SQL_ADD_TITLE = "insert into user_title (user_id, title_id) values (?,?)";
    //language=SQL
    private static final String SQL_FIND_ALL = "select * from \"user\"";
    //language=SQL
    private static final String SQL_FIND_BY_ID = "select * from \"user\" where id = ?";
    //language=SQL
    private static final String SQL_FIND_BY_EMAIL = "select * from \"user\" where email = ?";
    //language=SQL
    private static final String SQL_UPDATE_USER = "update \"user\" set nickname = ?, password = ?, email = ? where id = ?";
    //language=SQL
    private static final String SQL_INSERT_USER = "insert into \"user\" (nickname, password, email) values (?, ?, ?) returning id";
    //language=SQL
    private static final String SQL_DELETE_USER = "delete from \"user\" where id = ?";

    private DataSource dataSource;
    private SimpleJdbcTemplate<User> simpleJdbcTemplate;

    private RowMapper<User> userRowMapper = resultSet -> {
        User user = User.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .nickname(resultSet.getString("nickname"))
                .hashPassword(resultSet.getString("password"))
                .build();
        return user;
    };

    public UsersRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        this.simpleJdbcTemplate = new SimpleJdbcTemplate<>(dataSource);
    }

    @Override
    public User save(User entity) {
        User user = entity;
        try {
            Long id = simpleJdbcTemplate.queryInsertReturningId(
                    SQL_INSERT_USER,
                    user.getNickname(),
                    user.getHashPassword(),
                    user.getEmail()
            );
            user.setId(id);
        } catch (NoneUniqueValueException e) {
            user = null;
        }
        return user;
    }

    @Override
    public void update(User entity) {
        simpleJdbcTemplate.query(
                SQL_UPDATE_USER,
                entity.getNickname(),
                entity.getHashPassword(),
                entity.getEmail(),
                entity.getId()
        );
    }

    @Override
    public void delete(User entity) {
        simpleJdbcTemplate.query(SQL_DELETE_USER, entity.getId());
    }

    @Override
    public Optional<User> findById(Long id) {
        User user;
        try {
            List<User> users = simpleJdbcTemplate.query(SQL_FIND_BY_ID, userRowMapper, id);
            if (!users.isEmpty()) {
                user = users.get(0);
            } else {
                user = null;
            }
        } catch (IllegalStateException e) {
            user = null;
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> findAll() {
        return simpleJdbcTemplate.query(SQL_FIND_ALL, userRowMapper);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        User user;
        try {
            List<User> users = simpleJdbcTemplate.query(SQL_FIND_BY_EMAIL, userRowMapper, email);
            if (!users.isEmpty()) {
                user = users.get(0);
            } else {
                user = null;
            }
        } catch (IllegalStateException e) {
            user = null;
        }
        return Optional.ofNullable(user);
    }
}
