package carsharing.interfaces;

import carsharing.entity.Company;

import java.util.List;

public interface CompanyDao {
    List<Company> getAllCompanies();

    Company getCompany(String name);

    void createCompany(Company company);
}
