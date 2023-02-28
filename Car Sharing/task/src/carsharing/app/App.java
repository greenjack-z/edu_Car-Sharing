package carsharing.app;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;
import carsharing.interfaces.*;
import carsharing.utils.DbUtil;

import java.util.List;

public class App {
    private final DbUtil dbUtil;
    private final CompanyDao companyDao;
    private final CarDao carDao;

    private final CustomerDao customerDao;

    public App(String[] args) {
        String dbPath = "./src/carsharing/db/" + parseFileName(args);
        dbUtil = new DbUtil(dbPath);
        companyDao = new CompanyDaoH2(dbPath);
        carDao = new CarDaoH2(dbPath);
        customerDao = new CustomerDaoH2(dbPath);
    }

    private static String parseFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                return args[i + 1];
            }
        }
        return "defaultDb";
    }

    public void run() {
        dbUtil.initializeDb();
        new Menu(this).run();
    }

    public List<Company> getCompanies() {
        return companyDao.loadCompanies();
    }

    public Company getCompany(int id) {
        return companyDao.loadCompany(id);
    }

    public List<Car> getCars(Company company) {
        return carDao.loadCars(company);
    }

    public List<Car> getFreeCars(Company company) {
        return carDao.loadFreeCars(company);
    }

    public Car getCar(int id) {
        return carDao.loadCar(id);
    }

    public List<Customer> getCustomers() {
        return customerDao.loadCustomers();
    }

    public void createCompany(String name) {
        if (companyDao.createCompany(name)) {
            System.out.println("The company was created!");
        }
    }

    public void createCar(String name, Company company) {
        if(carDao.createCar(company, name)) {
            System.out.println("The car was created!");
        }
    }

    public void createCustomer(String name) {
        if (customerDao.createCustomer(name)) {
            System.out.println("The customer was created!");
        }
    }

    public void setCar(Customer customer) {
        customerDao.setCarId(customer);
    }
}
