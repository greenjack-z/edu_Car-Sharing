package carsharing.app;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.interfaces.CarDao;
import carsharing.interfaces.CarDaoH2;
import carsharing.interfaces.CompanyDao;
import carsharing.interfaces.CompanyDaoH2;
import carsharing.utils.DbUtil;

import java.util.Collections;

import java.util.List;

public class App {
    private final DbUtil dbUtil;
    private final CompanyDao companyDao;
    private final CarDao carDao;

    public App(String[] args) {
        String dbPath = "./src/carsharing/db/" + parseFileName(args);
        dbUtil = new DbUtil(dbPath);
        companyDao = new CompanyDaoH2(dbPath);
        carDao = new CarDaoH2(dbPath);
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

    public List<Car> getCars(Company company) {
        List<Car> list = carDao.loadCars(company);
        if (list.isEmpty()) {
            System.out.println("The car list is empty!");
            return Collections.emptyList();
        }
        return list;
    }

    public void createCompany(String name) {
        if (companyDao.createCompany(name)) {
            System.out.println("The company was created!");
        }
    }

    public void createCar(String name, Company company) {
        carDao.createCar(company, name);
    }
}
