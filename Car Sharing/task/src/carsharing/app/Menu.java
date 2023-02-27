package carsharing.app;

import carsharing.entity.Car;
import carsharing.entity.Company;
import carsharing.entity.Customer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Menu {
    private final Deque<Page> pages = new ArrayDeque<>();
    private boolean exit = false;
    private final App app;
    private BufferedReader bufferedReader;

    Menu(App app) {
        this.app = app;
    }

    public void run() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        pages.push(new Page(List.of(itemExit, itemLoginManager, itemLoginCustomer, itemCreateCustomer)));
        while (!exit) {
            printPage();
            try {
                int input = Integer.parseInt(bufferedReader.readLine());
                System.out.println();
                pages.getFirst().items.get(input).choose();
            } catch (NumberFormatException e) {
                System.out.println("please enter number");
            } catch (IndexOutOfBoundsException e) {
                System.out.println("please choose item from list");
            } catch (IOException e) {
                System.err.println("Input read error: " + e.getMessage());
            }
        }
    }

    private void printPage() {
        Page page = pages.getFirst();
        if (page.title != null) {
            System.out.println(page.title);
        }
        for (int i = 1; i < page.items.size(); i++) {
            System.out.printf("%d. %s%n", i, page.items.get(i));
        }
        System.out.printf("0. %s%n", page.items.get(0));
    }

    abstract static class Item {
        final String title;

        Item(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }

        abstract void choose();
    }
    Item itemExit = new Item("Exit") {
        @Override
        void choose() {
            exit = true;
        }
    };
    Item itemBack = new Item("Back") {
        @Override
        void choose() {
            pages.removeFirst();
        }
    };
    Item itemLoginManager = new Item("Log in as a manager") {
        @Override
        void choose() {
            pages.addFirst(new Page(List.of(itemBack, itemListCompanies, itemCreateCompany)));
        }
    };
    Item itemLoginCustomer = new Item("Log in as a customer") {
        @Override
        void choose() {
            List<Customer> customers = app.getCustomers();
            if (customers.isEmpty()) {
                System.out.println("The customer list is empty!");
                return;
            }
            List<Item> items = new ArrayList<>();
            items.add(itemBack);
            for (Customer customer : customers) {
                items.add(new Item(customer.name()) {
                    @Override
                    void choose() {
                        pages.removeFirst();
                        CustomerPage page = new CustomerPage(customer, List.of(itemBack, itemRentCar, itemReturnCar, itemRentedCar));
                        pages.addFirst(page);
                    }
                });
            }
            Page page = new Page(items);
            page.setTitle("Choose a customer:");
            pages.addFirst(page);
        }
    };
    Item itemCreateCustomer = new Item("Create a customer") {
        @Override
        void choose() {
            System.out.println("Enter the customer name:");
            try {
                app.createCustomer(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    Item itemListCompanies = new Item("Company list") {
        @Override
        void choose() {
            List<Company> companies = app.getCompanies();
            if (companies.isEmpty()) {
                System.out.println("The company list is empty!");
                return;
            }
            List<Item> items = new ArrayList<>();
            items.add(itemBack);
            for (Company company : companies) {
                items.add(new Item(company.name()) {
                    @Override
                    void choose() {
                        pages.removeFirst();
                        CompanyPage page = new CompanyPage(company, List.of(itemBack, itemListCars, itemCreateCar));
                        pages.addFirst(page);
                    }
                });
            }
            Page page = new Page(items);
            page.setTitle("Choose a company:");
            pages.addFirst(page);
        }
    };
    Item itemRentCar = new Item("Rent a car") {
        @Override
        void choose() {
            Customer customer = ((CustomerPage) pages.getFirst()).customer;
            List<Company> companies = app.getCompanies();
            if (companies.isEmpty()) {
                System.out.println("The company list is empty!");
                return;
            }
            if (customer.getCarId() != 0) {
                System.out.println("You've already rented a car!");
                return;
            }
            List<Item> companyItems = new ArrayList<>();
            companyItems.add(itemBack);
            for (Company company : companies) {
                companyItems.add(new Item(company.name()) {
                    @Override
                    void choose() {
                        List<Car> cars = app.getFreeCars(company);
                        if (cars.isEmpty()) {
                            System.out.println("The car list is empty!");
                            return;
                        }
                        List<Item> carItems = new ArrayList<>();
                        carItems.add(itemBack);
                        for (Car car : cars) {
                            carItems.add(new Item(car.name()) {
                                @Override
                                void choose() {
                                    customer.setCarId(car.id());
                                    app.setCar(customer);
                                    System.out.printf("You rented '%s'%n", car.name());
                                    pages.removeFirst();
                                    pages.removeFirst();
                                }
                            });
                        }
                        Page page = new Page(carItems);
                        page.setTitle("Choose a car:");
                        pages.addFirst(page);
                    }
                });
            }
            Page page = new Page(companyItems);
            page.setTitle("Choose a company:");
            pages.addFirst(page);
        }
    };
    Item itemCreateCompany = new Item("Create a company") {
        @Override
        void choose() {
            System.out.println("Enter the company name:");
            try {
                app.createCompany(bufferedReader.readLine());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    Item itemListCars = new Item("Car list") {
        @Override
        void choose() {
            Company company = ((CompanyPage) pages.getFirst()).company;
            List<Car> cars = app.getCars(company);
            if (cars.isEmpty()) {
                System.out.println("The car list is empty!");
                return;
            }
            System.out.printf("%s cars:%n", company.name());
            for (Car car : cars) {
                System.out.printf("%d. %s%n", cars.indexOf(car) + 1, car.name());
            }
            System.out.println();
        }
    };
    Item itemCreateCar = new Item("Create a car") {
        @Override
        void choose() {
            System.out.println("Enter the car name:");
            CompanyPage page = (CompanyPage) pages.getFirst();
            try {
                app.createCar(bufferedReader.readLine(), page.company);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
    Item itemReturnCar = new Item("Return a rented car") {
        @Override
        void choose() {
            CustomerPage page = (CustomerPage) pages.getFirst();
            if (page.customer.getCarId() == 0) {
                System.out.println("You didn't rent a car!");
                return;
            }
            page.customer.setCarId(0);
            app.setCar(page.customer);
            System.out.println("You've returned a rented car!");
        }
    };
    Item itemRentedCar = new Item("My rented car") {
        @Override
        void choose() {
            Customer customer = ((CustomerPage) pages.getFirst()).customer;
            if (customer.getCarId() == 0) {
                System.out.println("You didn't rent a car!");
                return;
            }
            Car car = app.getCar(customer.getCarId());
            Company company = app.getCompany(car.companyId());
            System.out.printf("Your rented car:%n%s%nCompany:%n%s%n%n", car.name(), company.name());
        }
    };

    static class Page {
        private final List<Item> items;
        private String title = null;

        Page(List<Item> items) {
            this.items = items;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

    static class CompanyPage extends Page {
        private final Company company;
        CompanyPage(Company company, List<Item> items) {
            super(items);
            this.setTitle(company.name() + " company:");
            this.company = company;
        }

        public Company getCompany() {
            return company;
        }
    }

    static class CustomerPage extends Page {
        private final Customer customer;
        CustomerPage(Customer customer, List<Item> items) {
            super(items);
            this.customer = customer;
        }
    }
}
