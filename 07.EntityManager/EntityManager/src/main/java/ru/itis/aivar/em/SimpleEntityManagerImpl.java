package ru.itis.aivar.em;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.itis.aivar.em.exceptions.RowMapperInitException;
import ru.itis.aivar.model.Id;
import ru.itis.aivar.em.queries.*;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class SimpleEntityManagerImpl implements EntityManager {

    private final Map<Class<?>, String> mapping;
    private final JdbcTemplate jdbcTemplate;

    public SimpleEntityManagerImpl(DataSource dataSource) {
        mapping = new HashMap<>();
        //Map of types
        mapping.put(Long.TYPE, "bigint");
        mapping.put(Long.class, "bigint");
        mapping.put(Integer.TYPE, "int");
        mapping.put(Integer.class, "int");
        mapping.put(Short.TYPE, "smallint");
        mapping.put(Short.class, "smallint");
        mapping.put(Byte.TYPE, "tinyint");
        mapping.put(Byte.class, "tinyint");
        mapping.put(Boolean.TYPE, "bit");
        mapping.put(Boolean.class, "bit");
        mapping.put(Float.TYPE, "real");
        mapping.put(Float.class, "real");
        mapping.put(Double.TYPE, "float");
        mapping.put(Double.class, "float");
        mapping.put(String.class, "text");
        mapping.put(Character.class, "varchar(1)");
        mapping.put(Character.TYPE, "varchar(1)");
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public <T> void createTable(String tableName, Class<T> entityClass) {
        Field[] fields = entityClass.getDeclaredFields();
        Map<String, String> fieldsMap = new HashMap<>();
        for (Field field : fields) {
            String sqlType = mapping.get(field.getType());
            if (field.isAnnotationPresent(Id.class)) {
                sqlType = sqlType.concat(" primary key");
            }
            fieldsMap.put(field.getName(), sqlType);
        }
        StringBuilder stringBuilder = new StringBuilder();
        Set<String> keySet = fieldsMap.keySet();
        //language=sql
        stringBuilder.append("create table ").append(tableName).append('(');
        int i = 0;
        for (String s : keySet) {
            stringBuilder.append(s).append(" ").append(fieldsMap.get(s));
            if (i != keySet.size() - 1) {
                stringBuilder.append(", ");
            }
            i++;
        }
        stringBuilder.append(");");
        jdbcTemplate.update(stringBuilder.toString());
    }

    @Override
    public <T> void createTable(Class<T> entityClass) {
        createTable(entityClass.getSimpleName(), entityClass);
    }

    @Override
    public void save(String tableName, Object entity) throws IllegalAccessException {
        Class<?> classOfEntity = entity.getClass();
        Field[] fields = classOfEntity.getDeclaredFields();
        Expression expression = Expression.create();
        for (Field field : fields) {
            field.setAccessible(true);
            boolean isText = field.getType().equals(Character.class) || field.getType().equals(Character.TYPE) ||
                    field.getType().equals(String.class);
            expression.addValues(field.getName(), field.get(entity).toString(), isText);
            field.setAccessible(false);
        }
        Query insertQuery = new InsertQuerySQL(tableName);
        insertQuery.setExpression(expression);
        jdbcTemplate.update(SQLQueryBuilder.buildSQLQuery(insertQuery));
    }

    @Override
    public <T, ID> T findById(Class<T> resultType, Class<ID> idType, ID idValue, String tableName) {
        RowMapper<T> rowMapper = (resultSet, i) ->
        {
            try {
                T entity = resultType.newInstance();
                Field[] fields = resultType.getDeclaredFields();
                for (Field field : fields) {
                    String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
                    Method method = resultType.getMethod(methodName, field.getType());
                    method.invoke(entity, resultSet.getObject(field.getName().toLowerCase()));
                }
                return entity;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RowMapperInitException(e);
            }
        };
        Query selectQuery = new SelectQuerySQL(tableName);
        boolean isText = idType.equals(Character.class) || idType.equals(Character.TYPE) || idType.equals(String.class);
        selectQuery.setExpression(Expression.create().addWhere("id", idValue.toString(), "=", isText));
        return jdbcTemplate.query(SQLQueryBuilder.buildSQLQuery(selectQuery), rowMapper).get(0);
    }

    @Override
    public <T> List<T> findAll(Class<T> entityClass) {
        return findAll(entityClass.getSimpleName(), entityClass);
    }

    @Override
    public <T> List<T> findAll(String tableName, Class<T> entityClass) {
        RowMapper<T> rowMapper = (resultSet, i) -> {
            try {
                T entity = entityClass.newInstance();
                Field[] fields = entityClass.getDeclaredFields();
                for (Field field : fields) {
                    String methodName = "set" + String.valueOf(field.getName().charAt(0)).toUpperCase() + field.getName().substring(1);
                    Method method = entityClass.getMethod(methodName, field.getType());
                    method.invoke(entity, resultSet.getObject(field.getName().toLowerCase()));
                }
                return entity;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                throw new RowMapperInitException(e);
            }
        };
        Query selectQuery = new SelectQuerySQL(tableName);
        return jdbcTemplate.query(SQLQueryBuilder.buildSQLQuery(selectQuery), rowMapper);
    }
}
