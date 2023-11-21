package soya.framework.commons.jdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseUtils {
    private DatabaseUtils() {
    }

    public static Object executeQuery(String sql, DataSource dataSource) {
        Connection connection = null;
        ResultSet rs = null;
        try {
            connection = connection(dataSource);
            rs = connection.createStatement().executeQuery(sql);

        } catch (SQLException e) {

        } finally {
            close(rs);
            close(connection);
        }

        return null;
    }


    private static Connection connection(DataSource dataSource) throws SQLException {
        return dataSource.getConnection();
    }

    private static void close(ResultSet resultSet) {
        try {
            if (resultSet != null && !resultSet.isClosed()) {
                resultSet.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void close(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
    }

}
