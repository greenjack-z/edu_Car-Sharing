package carsharing.interfaces;

import carsharing.entity.Car;
import carsharing.entity.Company;

import java.util.List;

public interface CarDao {
    Car loadCar(int id);
    List<Car> loadCars(Company company);
    List<Car> loadFreeCars(Company company);
    boolean createCar(Company company, String carName);
}
