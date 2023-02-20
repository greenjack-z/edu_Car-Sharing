package carsharing;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DbService {
    private final String dbUrl;

    private Connection connection;
    DbService (String fileName) {
        this.dbUrl = "jdbc:h2:./src/carsharing/db/" + fileName;
    }
    public void connect() {
        System.out.println(dbUrl);
        try {
            connection = DriverManager.getConnection(dbUrl);
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
