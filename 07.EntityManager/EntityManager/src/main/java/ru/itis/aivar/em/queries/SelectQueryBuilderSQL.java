package ru.itis.aivar.em.queries;

import java.util.ArrayList;
import java.util.List;

public class SelectQueryBuilderSQL implements SelectQueryBuilder {
    @Override
    public String createQuery(Query query) {
        String defaultTemplate = query.getDefaultTemplate();
        StringBuilder queryString = new StringBuilder(defaultTemplate);
        Expression expression = query.getExpression();
        if (expression.isEmpty()){
            return queryString.append(";").toString();
        }
        queryString.append(" ");
        List<Expression.Where> wheres = expression.getWhere();

        StringBuilder conditions = new StringBuilder();
        List<String> conditionsList = new ArrayList<>();

        for (Expression.Where where: wheres) {
            String whereStr = where.getValue();
            if (where.isText()){
                whereStr = "'"+whereStr+"'";
            }
            conditionsList.add(where.getKey().concat(where.getSeparator()).concat(whereStr));
        }

        conditions.append(String.join(", ", conditionsList));

        queryString.append("where").append(" ").append(conditions);
        return queryString.append(";").toString();
    }
}
