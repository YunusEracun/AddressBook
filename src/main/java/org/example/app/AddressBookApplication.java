package org.example.app;

import org.example.model.Company;
import org.example.model.Entry;
import org.example.model.Person;
import org.example.service.AddressBookService;
import org.example.util.Constants;
import org.example.util.InputManager;
import org.example.util.OperationResult;

import java.util.Collection;
import java.util.Scanner;

public class AddressBookApplication {

    private static final AddressBookService manager = new AddressBookService();
    private static final Scanner scanner = new Scanner(System.in);
    private static final InputManager inputManager = InputManager.getInstance(scanner);

    public static void main(String[] args) {
        manager.loadData();
        System.out.println("--- Welcome to the Console-Based Address Book Application ---");
        while (true) {
            showMenu();
            int choice = inputManager.getIntInput("Make your choice (1-7): ");
            handleAction(choice);
        }
    }

    private static void showMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Add New Entry");
        System.out.println("2. List All Entries");
        System.out.println("3. Search Entries");
        System.out.println("4. Delete Entry (by Email)");
        System.out.println("5. Find Duplicate Names/Company Names");
        System.out.println("6. Print Data as JSON");
        System.out.println("7. SAFE EXIT and SAVE");
        System.out.println("-------------");
    }

    private static void handleAction(int choice) {
        switch (choice) {
            case 1 -> addNewEntry();
            case 2 -> manager.listAllEntries();
            case 3 -> searchMenu();
            case 4 -> deleteEntry();
            case 5 -> checkDuplicates();
            case 6 -> printJsonOutput();
            case 7 -> {
                System.out.println("Exiting the application. Saving data...");
                manager.saveData();
                System.exit(0);
            }
            default -> System.out.println("Invalid choice. Please enter a number from the menu.");
        }
    }

    private static void addNewEntry() {
        System.out.println("/n--- Add New Entry ---");
        int typeChoice;
        do {
            typeChoice = inputManager.getIntInput("Select Entry Type (1: Person 2.Company");
            if (typeChoice != 1 && typeChoice != 2) {
                System.out.println("Invalid choice. Please enter a number from the menu.");
            }
        } while (typeChoice != 1 && typeChoice != 2);

        String name = inputManager.getStringInput("Enter Name: ");
        String email = inputManager.getStringInput("Enter Email: ");
        String phone = inputManager.getStringInput("Enter Phone Number: ");

        Entry newEntry = null;
        OperationResult result;

        try {
            if(typeChoice == 1) {
                String LastName = inputManager.getStringInput("Last Name: ");
                newEntry = new Person(name, email, phone, LastName);
            } else {
                String taxNumber = inputManager.getStringInput("Tax Number: ");
                String address = inputManager.getStringInput("Address: ");
                newEntry = new Company(name, email, phone, address, taxNumber);
            }
            result = manager.addEntry(newEntry);

            printMessage(result, newEntry.getName());
        } catch (IllegalArgumentException e) {
            System.out.println("VALIDATION ERROR: " + e.getMessage());
        }
    }

    private static void searchMenu() {
        System.out.println("\n--- SEARCH Entry TYPE ---");
        System.out.println("1. Search Person");
        System.out.println("2. Search Company");
        System.out.println("0. Back");

        int typeChoice = inputManager.getIntInput("Your choice (0-2): ");

        switch (typeChoice) {
            case 1 -> searchSubMenu("Person");
            case 2 -> searchSubMenu("Company");
            case 0 -> System.out.println("Returnıng to main menu.");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void  searchSubMenu(String entryType) {
        System.out.println("\n--- Search Fields for " + entryType.toUpperCase() + " ---");
        System.out.println("1. Search by Name");
        System.out.println("2. Search by Phone Number");
        System.out.println("3. Quick Search by Email (O(1))");

        if (entryType.equals("Person")) {
            System.out.println("4. Search by Last Name ");
        } else {
            System.out.println("4. Search by Tax Number");
            System.out.println("5. Search by Address ");
        }
        System.out.println("0. Back");

        int fieldChoice = inputManager.getIntInput("Your choice: ");
        String searchField="";

        switch (fieldChoice) {
            case 1 -> searchField ="name";
            case 2 -> searchField ="phone";
            case 3 -> {
                quickSearchByEmail();
                return;
            }
            case 4 -> {
                if(entryType.equals("Person")) {
                    searchField ="lastName";
                } else {
                    searchField ="taxNumber";
                }
            }
            case 5 -> {
                if(entryType.equals("Company")) {
                    searchField ="address";
                } else {
                    System.out.println("Invalid choice.");
                    return;
                }
            }
            case 0 -> {
                return;
            }
            default -> System.out.println("Invalid choice.");
        }
        searchAndPrint(entryType, searchField);

    }

    private static void deleteEntry() {
        System.out.println("\n--- Delete Person ---");
        String email = inputManager.getStringInput("Enter the email address of the person to delete: ");
        manager.deleteEntry(email);
    }

    private static void checkDuplicates() {
        Collection<Entry> duplicates = manager.findDuplicateNames();
        System.out.println("\n--- DUPLICATE RECORDS ---");
        if (duplicates.isEmpty()) {
            System.out.println("No duplicate records found.");
        } else {
            System.out.println(duplicates.size() + " duplicate record(s) found:");
            for (Entry p : duplicates) {
                System.out.println(p);
            }
        }
    }

    private static void printJsonOutput() {
        String jsonOutput = manager.convertBookToJson();
        System.out.println("\n--- JSON OUTPUT ---");
        System.out.println(jsonOutput);
        System.out.println("-------------------");
    }

    private static void searchAndPrint(String entryType, String searchField) {
        String searchValue = inputManager.getStringInput("Enter the " + searchField + " to search: ");

        // Service'teki yeni metodu çağırıyoruz
        Collection<Entry> results = manager.searchEntries(entryType, searchField, searchValue);

        System.out.println("\n--- SEARCH RESULTS (" + searchField.toUpperCase() + ") ---");
        if (results.isEmpty()) {
            System.out.println("No records found matching '" + searchValue + "'.");
        } else {
            System.out.println(results.size() + " record(s) found:");
            for (Entry entry : results) {
                System.out.println(entry);
            }
        }
        System.out.println("----------------------------------------");
    }

    private static void quickSearchByEmail() {
        String email = inputManager.getStringInput("Enter the email address to search: ");

        Entry found = manager.findEntryByEmail(email);

        System.out.println("\n--- EMAIL SEARCH RESULT ---");
        if (found != null) {
            System.out.println("Person found:");
            System.out.println(found);
        } else {
            System.out.println("ERROR: No record found with email '" + email + "'.");
        }
        System.out.println("----------------------------------");
    }

    private static void printMessage(OperationResult result, String... args) {
        String messageTemplate;

        switch (result) {
            case SUCCESS_ADD -> messageTemplate = Constants.SUCCESS_ADD;
            case ERROR_INVALID_EMAIL -> messageTemplate = Constants.ERROR_INVALID_EMAIL;
            case ERROR_INVALID_PHONE -> messageTemplate = Constants.ERROR_INVALID_PHONE;
            case ERROR_DUPLICATE_EMAIL -> messageTemplate = Constants.ERROR_DUPLICATE_EMAIL;
            case ERROR_DUPLICATE_PHONE -> messageTemplate = Constants.ERROR_DUPLICATE_PHONE;
            case ERROR_INVALID_NAME_SURNAME -> messageTemplate = Constants.ERROR_INVALID_NAME_SURNAME;
            case INFO_NO_FILE_FOUND -> messageTemplate = Constants.INFO_NO_FILE_FOUND;
            case ERROR_DURING_SAVE -> messageTemplate = Constants.ERROR_DURING_SAVE;
            case SUCCESS_DELETE -> messageTemplate = Constants.SUCCESS_DELETE;
            case INFO_NO_RECORD_FOUND -> messageTemplate = Constants.INFO_NO_MATCH_FOUND;
            default -> messageTemplate = "ERROR: Unknown operation result (" + result.name() + ")";
        }

        System.out.println(String.format(messageTemplate, args));
    }
}
