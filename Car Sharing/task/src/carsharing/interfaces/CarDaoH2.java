package carsharing.interfaces;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.util.List;

public class CarDaoH2 implements CarDao {
    private final String dbUrl;

    public CarDaoH2(String dbPath) {
        this.dbUrl = "jdbc:h2:" + dbPath;
    }
    @Override
    public Car loadCar(int id) {
        return null;
    }

    @Override
    public List<Car> loadCars(Company company) {
        return null;
    }

    @Override
    public void createCar(Company company, String carName) {

    }
}
