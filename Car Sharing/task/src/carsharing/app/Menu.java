package carsharing.app;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Menu {
    private page currentPage = page.WELCOME;

    public void run(App app) {
        boolean exit = false;
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in))) {
            while (!exit) {
                printPage();
                int input = Integer.parseInt(bufferedReader.readLine());
                if (input > currentPage.items.length) {
                    System.out.println("Please choose the listed option!");
                    continue;
                }
                switch (currentPage.items[input]) {
                    case COMPANY_LIST -> app.listCompanies();
                    case COMPANY_CREATE -> app.createCompany(promptName(bufferedReader));
                    case LOGIN -> currentPage = page.MANAGE;
                    case BACK -> currentPage = page.WELCOME;
                    case EXIT -> exit = true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Please enter the number!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printPage() {
        for (int i = 1; i < currentPage.items.length; i++) {
            System.out.printf("%d. %s%n", i, currentPage.items[i]);
        }
        System.out.printf("0. %s%n", currentPage.items[0]);
    }

    private String promptName(BufferedReader bufferedReader) throws IOException {
        System.out.println("Enter the company name:");
        return bufferedReader.readLine();
    }

    enum item {
        EXIT("Exit"),
        BACK("Back"),
        LOGIN("Log in as a manager"),
        COMPANY_LIST("Company list"),
        COMPANY_CREATE("Create a company");
        private final String title;

        item(String title) {
            this.title = title;
        }

        @Override
        public String toString() {
            return title;
        }
    }

    enum page {
        WELCOME(new item[]{item.EXIT, item.LOGIN}),
        MANAGE(new item[]{item.BACK, item.COMPANY_LIST, item.COMPANY_CREATE});
        private final item[] items;

        page(item[] items) {
            this.items = items;
        }
    }
}
