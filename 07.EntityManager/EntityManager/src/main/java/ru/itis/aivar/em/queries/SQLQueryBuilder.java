package ru.itis.aivar.em.queries;

public class SQLQueryBuilder {

    public static String buildSQLQuery(Query query) {
        QueryBuilder queryBuilder = query.getBuilder();
        return queryBuilder.createQuery(query);
    }

}