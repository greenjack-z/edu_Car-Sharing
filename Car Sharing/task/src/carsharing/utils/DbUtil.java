package carsharing.utils;

import java.sql.*;

public class DbUtil {
    private final String dbUrl;

    public DbUtil(String dbPath) {
        this.dbUrl = "jdbc:h2:" + dbPath;
    }

    public void initializeDb() {
        try (Connection connection = DriverManager.getConnection(dbUrl)) {
            connection.setAutoCommit(true);
            createCompanyTable(connection);
            createCarTable(connection);
        } catch (SQLException e) {
            System.err.println("dbAccess error: " + e.getMessage());
        }
    }

    private void createCompanyTable(Connection connection) {
        String sql = """
                CREATE TABLE IF NOT EXISTS company (
                    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR NOT NULL UNIQUE
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL error");
            e.printStackTrace();
        }
    }

    private void createCarTable(Connection connection) {
        String sql = """
                CREATE TABLE IF NOT EXISTS car (
                    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR NOT NULL UNIQUE,
                    company_id INTEGER NOT NULL,
                    FOREIGN KEY (company_id) REFERENCES company(id)
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL error");
            e.printStackTrace();
        }
    }

}
