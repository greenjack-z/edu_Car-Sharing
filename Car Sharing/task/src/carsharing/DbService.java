package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbService {
    private final String dbUrl;
    private final String user;
    private final String password;

    private Connection connection;
    DbService (String dbUrl, String user, String password) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;
    }
    public void connect() {
        try {
            connection = DriverManager.getConnection(dbUrl, user, password);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("JDBC error");
            e.printStackTrace();
        }
    }

    public void createTable() {
        //todo make method with parameters to create any table
        String sql = """
                CREATE TABLE company (
                    id INTEGER NOT NULL,
                    name VARCHAR
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL error");
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("DB connection close error");
            e.printStackTrace();
        }
    }
}
