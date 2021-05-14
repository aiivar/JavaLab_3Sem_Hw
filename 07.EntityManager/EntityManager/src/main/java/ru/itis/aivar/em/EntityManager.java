package ru.itis.aivar.em;

import java.util.List;

public interface EntityManager {

    <T> void createTable(String tableName, Class<T> entityClass);

    <T> void createTable(Class<T> entityClass);

    void save(String tableName, Object entity) throws IllegalAccessException;

    <T, ID> T findById(Class<T> resultType, Class<ID> idType, ID idValue, String tableName);

    <T> List<T> findAll(Class<T> entityClass);
    <T> List<T> findAll(String tableName, Class<T> entityClass);
}
