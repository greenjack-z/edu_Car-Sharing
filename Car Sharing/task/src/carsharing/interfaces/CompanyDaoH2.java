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

    public Connection connect() {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("JDBC error");
            e.printStackTrace();
        }
        return connection;
    }
    public void close(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            System.err.println("DB connection close error");
            e.printStackTrace();
        }
    }


    public Company loadCompany(int id) {
        Company company = null;
        String sql = "SELECT id, name FROM company WHERE id = ?";
        Connection connection = connect();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            company = new Company(resultSet.getInt("id"), resultSet.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return company;
    }
    @Override
    public List<Company> loadCompanies() {
        List<Company> companies = new ArrayList<>();
        String sql = "SELECT id, name FROM company ORDER by id";
        Connection connection = connect();
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                companies.add(new Company(resultSet.getInt("id"), resultSet.getString("name")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            close(connection);
        }
        return companies;
    }

    @Override
    public boolean createCompany(String name) {
        String sql = "INSERT INTO company(name) VALUES (?)";
        Connection connection = connect();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
            close(connection);
        }
    }
}
