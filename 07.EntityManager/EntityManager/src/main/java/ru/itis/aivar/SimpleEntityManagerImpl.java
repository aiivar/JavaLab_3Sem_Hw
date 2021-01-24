package ru.itis.aivar;

import ru.itis.aivar.model.Id;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SimpleEntityManagerImpl {

    private final Map<Class<?>, String> mapping;
    private final DataSource dataSource;
    private final SimpleJdbcTemplate<Class<?>> simpleJdbcTemplate;

    public SimpleEntityManagerImpl(DataSource dataSource) {
        this.dataSource = dataSource;
        mapping = new HashMap<>();
        simpleJdbcTemplate = new SimpleJdbcTemplate<>(dataSource);
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
        simpleJdbcTemplate.query(stringBuilder.toString());
    }

    public void save(String tableName, Object entity) throws IllegalAccessException {
        Class<?> classOfEntity = entity.getClass();
        Field[] fields = classOfEntity.getDeclaredFields();
        StringBuilder stringBuilder = new StringBuilder();
        //language=sql
        stringBuilder.append("insert into ").append(tableName).append(" (");
        for (int i = 0; i < fields.length; i++) {
            stringBuilder.append(fields[i].getName());
            if (i != fields.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(") ").append(" values (");
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            String value = field.get(entity).toString();
            if (
                    field.getType().equals(Character.class) || field.getType().equals(Character.TYPE) ||
                            field.getType().equals(String.class)
            ) {
                value = "'" + value + "'";
            }
            stringBuilder.append(value);
            if (i != fields.length - 1) {
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(");");
        simpleJdbcTemplate.query(stringBuilder.toString());
    }

    public <T, ID> T findById(Class<T> resultType, Class<ID> idType, ID idValue, String tableName) {
        RowMapper<T> rowMapper = resultSet -> {
            try {
                T entity = resultType.newInstance();
                Field[] fields = resultType.getDeclaredFields();
                for(Field field: fields){
                    field.setAccessible(true);
                    field.set(entity, resultSet.getObject(field.getName().toLowerCase()));
                }
                return entity;
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException(e);
            }
        };

        //language=sql
        String sql = "select * from "+tableName+" where id=?;";

        String id;
        if (idType.equals(String.class) || idType.equals(Character.class) || idType.equals(Character.TYPE)){
            return simpleJdbcTemplate.query(sql, rowMapper, idValue.toString()).get(0);
        }else {
            return simpleJdbcTemplate.query(sql, rowMapper, idValue).get(0);
        }
    }
}
