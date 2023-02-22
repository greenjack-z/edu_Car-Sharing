package carsharing.interfaces;

import carsharing.entity.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CompanyDaoH2 implements CompanyDao {
    private final String dbUrl;

    public CompanyDaoH2(String dbPath) {
        this.dbUrl = "jdbc:h2:" + dbPath;
    }


    static class ConnectionException extends RuntimeException {
    }
    private Connection connection() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }
    @Override
    public Company loadCompany(int id) {
        Company company = null;
        String sql = "SELECT id, name FROM company WHERE id = ?";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            company = new Company(resultSet.getInt("id"), resultSet.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return company;
    }
    @Override
    public List<Company> loadCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT id, name FROM company ORDER by id";
        try (Connection connection = connection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return companies;
    }

    @Override
    public boolean createCompany(String name) {
        String sql = "INSERT INTO company(name) VALUES (?)";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
