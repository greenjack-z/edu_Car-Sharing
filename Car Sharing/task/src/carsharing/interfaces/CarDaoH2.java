package carsharing.interfaces;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.utils.ConnectionException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CarDaoH2 implements CarDao {
    private final String dbUrl;

    public CarDaoH2(String dbPath) {
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
    public Car loadCar(int id) {
        Car car = null;
        String sql = "SELECT id, name, company_id FROM car WHERE id = ?";
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            car = new Car(resultSet.getInt("id"), resultSet.getString("name"), resultSet.getInt("company_id"));
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
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"), company.id()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cars;
    }

    @Override
    public List<Car> loadFreeCars(Company company) {
        List<Car> cars = new ArrayList<>();
        String sql = """
                SELECT car.id, car.name
                FROM car LEFT JOIN customer ON car.id = customer.rented_car_id
                WHERE rented_car_id IS NULL AND company_id = ?
                ORDER by id
                """;
        try (Connection connection = connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, company.id());
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                cars.add(new Car(resultSet.getInt("id"), resultSet.getString("name"), company.id()));
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
