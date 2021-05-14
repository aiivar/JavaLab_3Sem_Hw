package ru.itis.aivar.em.queries;

import java.util.ArrayList;
import java.util.List;

public class InsertQueryBuilderSQL implements InsertQueryBuilder {
    @Override
    public String createQuery(Query query) {
        String defaultTemplate = query.getDefaultTemplate();
        StringBuilder queryString = new StringBuilder(defaultTemplate);
        Expression expression = query.getExpression();
        if (expression.isEmpty()) {
            return queryString.append(";").toString();
        }
        queryString.append(" ");
        List<Expression.Value> values = expression.getValues();
        StringBuilder valuesNames = new StringBuilder();
        StringBuilder valuesValues = new StringBuilder();

        List<String> valuesNamesList = new ArrayList<>();
        List<String> valuesValuesList = new ArrayList<>();

        valuesValues.append("(");
        valuesNames.append("(");
        for (Expression.Value value : values) {
            valuesNamesList.add(value.getKey());
            String valueStr = value.getValue();
            if (value.isText()){
                valueStr = "'"+valueStr+"'";
            }
            valuesValuesList.add(valueStr);
        }
        valuesNames.append(String.join(", ", valuesNamesList)).append(")");
        valuesValues.append(String.join(", ", valuesValuesList)).append(")");
        queryString.append(valuesNames).append(" values ").append(valuesValues);
        return queryString.append(";").toString();
    }
}
