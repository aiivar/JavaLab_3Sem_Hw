package ru.itis.aivar;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.itis.aivar.model.Student;

import javax.sql.DataSource;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        SimpleEntityManagerImpl entityManager = new SimpleEntityManagerImpl(getDataSource());
//        entityManager.createTable("student", Student.class);
//        Student student = new Student(1L ,"Aivar", "Minsafin", 19);
//        try {
//            entityManager.save("student", student);
//        } catch (IllegalAccessException e) {
//            throw new IllegalStateException(e);
//        }
        System.out.println(entityManager.findById(Student.class, Long.class, 1L, "student"));
    }

    public static DataSource getDataSource(){
        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream("db.properties"));
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(properties.getProperty("db.jdbc-url"));
        hikariConfig.setDriverClassName(properties.getProperty("db.driver-class-name"));
        hikariConfig.setUsername(properties.getProperty("db.username"));
        hikariConfig.setPassword(properties.getProperty("db.password"));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(properties.getProperty("db.max-pool-size")));
        HikariDataSource dataSource = new HikariDataSource(hikariConfig);
        return dataSource;
    }
}
