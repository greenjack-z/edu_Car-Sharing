package carsharing.interfaces;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.util.List;

public interface CarDao {
    Car loadCar(int id);
    List<Car> loadCars(Company company);
    void createCar(Company company, String carName);
}
