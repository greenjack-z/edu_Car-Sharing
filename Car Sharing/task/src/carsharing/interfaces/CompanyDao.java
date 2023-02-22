package carsharing.interfaces;

import carsharing.entity.Company;

import java.util.List;

public interface CompanyDao {
    Company loadCompany(int id);
    List<Company> loadCompanies();
    boolean createCompany(String name);
}
