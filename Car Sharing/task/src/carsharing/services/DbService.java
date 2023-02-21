package carsharing.services;

import carsharing.entity.Company;
import carsharing.interfaces.CompanyDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbService implements CompanyDao {
    private final String dbUrl;

    private Connection connection;

    public DbService(String fileName) {
        this.dbUrl = "jdbc:h2:./src/carsharing/db/" + fileName;
        this.createTable();
    }

    public void connect() {
        try {
            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("JDBC error");
            e.printStackTrace();
        }
    }

    public void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS company (
                    id INTEGER NOT NULL PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR NOT NULL UNIQUE
                );
                """;
        connect();
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            System.err.println("SQL error");
            e.printStackTrace();
        } finally {
            close();
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

    @Override
    public List<Company> getAllCompanies() {
        List<Company> list = new ArrayList<>();
        String sql = "SELECT name FROM company";
        connect();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                list.add(new Company(resultSet.getString(1)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }

    @Override
    public Company getCompany(String name) {
        String sql = "SELECT name FROM company WHERE name = ?";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            ResultSet resultSet = statement.executeQuery();
            return new Company(resultSet.getString(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
        return null;
    }

    @Override
    public void createCompany(Company company) {
        String sql = "INSERT INTO company(name) VALUES (?)";
        connect();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, company.name());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close();
        }
    }
}
