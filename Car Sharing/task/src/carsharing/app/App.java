package carsharing.app;

import carsharing.entity.Company;
import carsharing.interfaces.CompanyDao;
import carsharing.services.DbService;

import java.util.List;

public class App {
    private final CompanyDao companyDao;

    public App(String[] args) {
        String dbFileName = parseFileName(args);
        companyDao = new DbService(dbFileName);
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
        new Menu().run(this);
    }

    public void listCompanies() {
        List<Company> list = companyDao.getAllCompanies();
        if (list.isEmpty()) {
            System.out.printf("The company list is empty!%n");
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            System.out.printf("%d. %s%n", i + 1, list.get(i).name());
        }
        System.out.println();
    }

    public void createCompany(String name) {
        companyDao.createCompany(new Company(name));
    }
}
