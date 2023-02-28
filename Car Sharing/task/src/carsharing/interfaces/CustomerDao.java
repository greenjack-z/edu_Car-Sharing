package carsharing.interfaces;

import carsharing.entity.Customer;

import java.util.List;

public interface CustomerDao {
     List<Customer> loadCustomers();
     boolean createCustomer(String name);

     int getCarId (Customer customer);
     void setCarId (Customer customer);
}
