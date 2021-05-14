package ru.itis.aivar.em.queries;

public interface Query {

    String getDefaultTemplate();

    Expression getExpression();

    boolean isReturning();

    void setExpression(Expression expression);

    QueryBuilder getBuilder();

}
