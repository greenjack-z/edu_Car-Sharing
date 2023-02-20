package carsharing;

public class Main {

    public static void main(String[] args) {
        new Main().run(getFileName(args));
    }

    private static String getFileName(String[] args) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-databaseFileName")) {
                return args[i + 1];
            }
        }
        return "defaultDB.db";
    }

    private void run(String filename) {
        final String userName = "user";
        final String password = "";
        DbService dbService = new DbService(filename, userName, password);
        dbService.connect();
        dbService.createTable();
        dbService.close();
    }
}