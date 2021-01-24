package ru.itis.aivar.repositories;

import ru.itis.aivar.models.CookieMock;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

public class CookiesRepositoryImpl implements CookiesRepository{
    //language=sql
    private static final String SQL_INSERT = "insert into user_cookie (uuid, user_id) values (?, ?)";
    //language=sql
    private static final String SQL_FIND_BY_UUID = "select * from user_cookie where uuid = ?";
    //language=sql
    private static final String SQL_DELETE = "delete from user_cookie where user_id = ?";

    RowMapper<CookieMock> rowMapper = resultSet -> CookieMock.builder()
            .userId(resultSet.getLong("user_id"))
            .uuid(resultSet.getString("uuid"))
            .build();

    SimpleJdbcTemplate<CookieMock> simpleJdbcTemplate;

    public CookiesRepositoryImpl(DataSource dataSource) {
        simpleJdbcTemplate = new SimpleJdbcTemplate<>(dataSource);
    }

    @Override
    public CookieMock findByUUID(String uuid) {
        List<CookieMock> list = simpleJdbcTemplate.query(SQL_FIND_BY_UUID, rowMapper, uuid);
        return !list.isEmpty()?list.get(0):null;
    }

    @Override
    public CookieMock save(CookieMock entity) {
        simpleJdbcTemplate.query(SQL_DELETE, entity.getUserId());
        simpleJdbcTemplate.query(SQL_INSERT, entity.getUuid(), entity.getUserId());
        return null;
    }

    @Override
    public void update(CookieMock entity) {

    }

    @Override
    public void delete(CookieMock entity) {

    }

    @Override
    public Optional<CookieMock> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<CookieMock> findAll() {
        return null;
    }
}
