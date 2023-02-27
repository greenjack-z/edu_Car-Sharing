package carsharing.interfaces;

import carsharing.entity.Customer;
import carsharing.utils.ConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoH2 implements CustomerDao {
    private final String dbUrl;
    public CustomerDaoH2(String dbPath) {
        this.dbUrl = "jdbc:h2:" + dbPath;
    }
    private Connection connection() {
        try {
            Connection connection = DriverManager.getConnection(dbUrl);
            connection.setAutoCommit(true);
            return connection;
        } catch (SQLException e) {
            throw new ConnectionException(e);
        }
    }

    @Override
    public List<Customer> loadCustomers() {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT id, name, rented_car_id FROM customer ORDER by id";
        try (Connection connection = connection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                customers.add(new Customer(resultSet.getInt(1), resultSet.getString(2), resultSet.getInt(3)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customers;
    }

    @Override
    public boolean createCustomer(String name) {
        String sql = "INSERT INTO customer(name) VALUES (?)";
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

    @Override
    public int getCarId(Customer customer) {
        int id = 0;
        String sql = "SELECT rented_car_id FROM customer WHERE id = ?";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, customer.id());
            ResultSet resultSet = statement.executeQuery();
            id = resultSet.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

    @Override
    public void setCarId(Customer customer) {
        String sql = "UPDATE customer SET rented_car_id = ? WHERE id = ?";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            if (customer.getCarId() == 0) {
                statement.setNull(1, Types.INTEGER);
            } else {
                statement.setInt(1, customer.getCarId());
            }
            statement.setInt(2, customer.id());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
