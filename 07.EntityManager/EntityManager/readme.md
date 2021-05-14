# Simple ORM

### Entity manager

Основной задачей было реализовать *ORM* для работы с простыми сущностями. Эта *ORM* работает с *POJO* классами.

```java
public class Student {
    @Id // Аннотация для primary key
    private Long id;
    private String firstName;
    private String lastName;
    private int age;
}
```

Запросы описываются интерфейсом `Query`

```java
public interface Query {

    String getDefaultTemplate();

    Expression getExpression();

    boolean isReturning();

    void setExpression(Expression expression);

    QueryBuilder getBuilder();

}
```
[Пример реализации]

На данный момент предусмотрены и реализованы `SelectQuery` и `InsertQuery`

Условия запросов описывает класс `Expression`

```java
public class Expression {

    private List<Where> where;
    private List<Value> values;
    private boolean isEmpty;

    //Constructor and factory method

    public Expression addWhere(String key, String value, String separator, boolean isText) {
        if (isEmpty) isEmpty = false;
        where.add(new Where(key, value, separator, isText));
        return this;
    }

    //Other addWhere overload

    public Expression addValues(String key, String value, boolean isText) {
        if (isEmpty) isEmpty = false;
        values.add(new Value(key, value, isText));
        return this;
    }

    //Other addValues overload

    //Getters and setters

    class Where {
        //Where POJO
    }

    class Value {
        //Value POJO
    }
}
```

Запрос формируется с помощью `QueryBuilder` и `SQLQueryBuilder`

```java
public class SQLQueryBuilder {

    public static String buildSQLQuery(Query query) {
        QueryBuilder queryBuilder = query.getBuilder();
        return queryBuilder.createQuery(query);
    }

}
```

```java
public interface QueryBuilder {

    String createQuery(Query query);

}
```
[Пример реализации]

Реализация `Query` содержит поддерживающюю его реализацию `QueryBuilder`

```java
public interface Query {
    
    //Other methods
    QueryBuilder getBuilder();
    
}
```

[Пример реализации]

Интерфейс `EntityManager`:

```java
public interface EntityManager {
    
    <T> void createTable(String tableName, Class<T> entityClass);

    <T> void createTable(Class<T> entityClass);

    void save(String tableName, Object entity) throws IllegalAccessException;

    <T, ID> T findById(Class<T> resultType, Class<ID> idType, ID idValue, String tableName);

    <T> List<T> findAll(Class<T> entityClass);

    <T> List<T> findAll(String tableName, Class<T> entityClass);
    
}
```
[Пример реализации]