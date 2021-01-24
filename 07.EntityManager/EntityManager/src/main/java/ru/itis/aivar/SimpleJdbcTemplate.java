package ru.itis.aivar;

import ru.itis.aivar.db.NoneUniqueValueException;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SimpleJdbcTemplate<T> {

    private DataSource dataSource;

    public SimpleJdbcTemplate(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public Long queryInsertReturningId(String sql , Object ... args){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            int pos = 1;
            for (Object o: args) {
                statement.setObject(pos, o);
                pos++;
            }
            resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
        } catch (SQLException e) {
            if (e.getErrorCode() == 23505){
                throw new NoneUniqueValueException(e);
            }
            throw new IllegalStateException(e);
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
        }
    }

    public void query(String sql , Object ... args){
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            int pos = 1;
            for (Object o: args) {
                statement.setObject(pos, o);
                pos++;
            }
            statement.execute();
        } catch (SQLException e) {
            if (e.getErrorCode() == 23505){
                throw new NoneUniqueValueException(e);
            }
            throw new IllegalStateException(e);
        } finally {
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
        }
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper, Object ... args){
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = dataSource.getConnection();
            statement = connection.prepareStatement(sql);
            int pos = 1;
            for (Object o: args) {
                statement.setObject(pos, o);
                pos++;
            }

            resultSet = statement.executeQuery();

            List<T> result = new ArrayList<>();
            while (resultSet.next()){
                result.add(rowMapper.mapRow(resultSet));
            }

            return result;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        } finally {
            if (resultSet != null){
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (statement != null){
                try {
                    statement.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
            if (connection != null){
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    //ignore
                }
            }
        }
    }

}
