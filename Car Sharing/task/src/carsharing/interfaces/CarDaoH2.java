package carsharing.interfaces;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoH2 implements CarDao {
    private final String dbUrl;

    public CarDaoH2(String dbPath) {
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
    public Car loadCar(int id) {
        Car car = null;
        String sql = """
                    SELECT id, car.name, company.name
                    FROM car JOIN company ON company_id = company.id
                    WHERE id = ?
                    """;
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            car = new Car(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return car;
    }

    @Override
    public List<Car> loadCars(Company company) {
        List<Car> cars = new ArrayList<>();
        String sql = "SELECT id, name FROM car WHERE company_id = ? ORDER by id";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, company.id());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"), company.name()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public boolean createCar(Company company, String carName) {
        String sql = "INSERT INTO car(name, company_id) VALUES (?, ?)";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, carName);
            statement.setInt(2, company.id());
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
