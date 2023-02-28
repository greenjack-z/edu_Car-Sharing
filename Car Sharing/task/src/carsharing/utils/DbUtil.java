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
            createCompanyTable();
            createCarTable();
            createCustomerTable();
        } catch (SQLException e) {
            System.err.println("dbAccess error: " + e.getMessage());
        }
    }

    private Connection getConnection() {
        try {
            return DriverManager.getConnection(dbUrl);
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    private void createCompanyTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS company (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(50) NOT NULL UNIQUE
                );
                """;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createCarTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS car (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(20) NOT NULL UNIQUE,
                    company_id INT NOT NULL REFERENCES company
                );
                """;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL error");
            e.printStackTrace();
        }
    }

    private void createCustomerTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS customer (
                id INT PRIMARY KEY AUTO_INCREMENT,
                name VARCHAR(30) NOT NULL UNIQUE,
                rented_car_id INT REFERENCES car
                );
                """;
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
