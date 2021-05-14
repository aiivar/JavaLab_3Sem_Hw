package ru.itis.aivar.em.queries;

public class InsertQuerySQL implements InsertQuery {

    private Expression expression;
    private String tableName;
    private final String defaultTemplate = "insert into ";

    public InsertQuerySQL(String tableName) {
        this.tableName = tableName;
        this.expression = Expression.create();
    }

    @Override
    public String getDefaultTemplate() {
        return defaultTemplate.concat(tableName);
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean isReturning() {
        return false;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public QueryBuilder getBuilder() {
        return new InsertQueryBuilderSQL();
    }
}
