package ru.itis.aivar.em.queries;

public class SelectQuerySQL implements SelectQuery {

    private Expression expression;
    private String tableName;
    private final String defaultTemplate = "select * from";

    public SelectQuerySQL(String tableName) {
        this.tableName = tableName;
        this.expression = Expression.create();
    }

    @Override
    public String getDefaultTemplate() {
        return defaultTemplate.concat(" ").concat(tableName);
    }

    @Override
    public Expression getExpression() {
        return expression;
    }

    @Override
    public boolean isReturning() {
        return true;
    }

    @Override
    public void setExpression(Expression expression) {
        this.expression = expression;
    }

    @Override
    public QueryBuilder getBuilder() {
        return new SelectQueryBuilderSQL();
    }
}
