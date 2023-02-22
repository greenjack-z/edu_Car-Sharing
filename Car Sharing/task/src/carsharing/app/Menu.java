package carsharing.app;

import carsharing.entity.Company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class Menu {
    //private Page currentPage;
    private final Deque<Page> pages = new ArrayDeque<>();
    private boolean exit = false;
    private final App app;
    private BufferedReader bufferedReader;

    Menu(App app) {
        this.app = app;
    }

    public void run() {
        bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        pages.push(welcomePage);
        try {
            while (!exit) {
                printPage();
                int input = Integer.parseInt(bufferedReader.readLine());
                pages.getFirst().items.get(input).choose();
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter the number!");
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Please choose the item from list");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
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

    abstract class Item {
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
    Item itemLogin = new Item("Log in as a manager") {
        @Override
        void choose() {
            pages.addFirst(managerPage);
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
                        companyPage.title = company.name() + " company:";
                        pages.removeFirst();
                        pages.addFirst(companyPage);
                    }
                });
            }
            pages.addFirst(new Page(items));
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
            pages.addFirst(new Page(List.of(testItem, testItem)));
        }
    };
    Item itemCreateCar = new Item("Create a car") {
        @Override
        void choose() {
            System.out.println("%nEnter the car name:%n");
            try {
                app.createCar(bufferedReader.readLine(), new Company(0, "null"));//todo
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    Item testItem = new Item("default test item") {
        @Override
        void choose() {
            System.out.println("test item choosen");
        }
    };


    class Page {
        private final List<Item> items;
        private String title = null;

        Page(List<Item> items) {
            this.items = items;
        }
    }
    Page welcomePage = new Page(List.of(itemExit, itemLogin));
    Page managerPage = new Page(List.of(itemBack, itemListCompanies, itemCreateCompany));
    Page companyPage = new Page(List.of(itemBack, itemListCars, itemCreateCar));
}
