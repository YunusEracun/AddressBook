package org.example;

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
        System.out.println("1. Add New Person");
        System.out.println("2. List All People");
        System.out.println("3. Search Person");
        System.out.println("4. Delete Person (by Email)");
        System.out.println("5. Find Duplicate Names");
        System.out.println("6. Print Data as JSON");
        System.out.println("7. SAFE EXIT and SAVE");
        System.out.println("-------------");
    }

    private static void handleAction(int choice) {
        switch (choice) {
            case 1 -> addNewPerson();
            case 2 -> manager.listAllPeople();
            case 3 -> searchMenu();
            case 4 -> deletePerson();
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

    private static void addNewPerson() {
        System.out.println("\n--- Add New Person ---");

        String firstName = inputManager.getStringInput("First Name: ");
        String lastName = inputManager.getStringInput("Last Name: ");
        String phone = inputManager.getStringInput("Phone Number: ");
        String email = inputManager.getStringInput("Email Address: ");

        Person newPerson = new Person(firstName, lastName, phone, email);

        OperationResult result = manager.addPerson(newPerson);

        switch (result) {
            case SUCCESS_ADD:
                printMessage(result, newPerson.getFirstName());
                break;

            case ERROR_INVALID_EMAIL:
                String domains = String.join(", ", Constants.VALID_EMAIL_DOMAINS);
                printMessage(result, domains);
                break;

            case ERROR_INVALID_PHONE:
                printMessage(result);
                break;

            case ERROR_DUPLICATE_EMAIL:
                printMessage(result, newPerson.getEmail());
                break;

            case ERROR_DUPLICATE_PHONE:
                printMessage(result, newPerson.getPhoneNumber());
                break;

            case ERROR_INVALID_NAME_SURNAME:
                printMessage(result);
                break;

            default:
                System.err.println("CRITICAL ERROR: Unexpected operation result: " + result);
                break;
        }
    }

    private static void searchMenu() {
        System.out.println("\n--- SEARCH TYPE ---");
        System.out.println("1. Search by First Name");
        System.out.println("2. Search by Last Name");
        System.out.println("3. Search by Phone Number");
        System.out.println("4. Quick Search by Email");
        System.out.println("0. Back");

        int subChoice = inputManager.getIntInput("Your choice (0-4): ");

        switch (subChoice) {
            case 1 -> searchAndPrint("firstname");
            case 2 -> searchAndPrint("lastname");
            case 3 -> searchAndPrint("phone");
            case 4 -> quickSearchByEmail();
            case 0 -> System.out.println("Returning to main menu.");
            default -> System.out.println("Invalid choice.");
        }
    }

    private static void deletePerson() {
        System.out.println("\n--- Delete Person ---");
        String email = inputManager.getStringInput("Enter the email address of the person to delete: ");
        manager.deletePerson(email);
    }

    private static void checkDuplicates() {
        Collection<Person> duplicates = manager.findDuplicateNames();
        System.out.println("\n--- DUPLICATE RECORDS ---");
        if (duplicates.isEmpty()) {
            System.out.println("No duplicate records found.");
        } else {
            System.out.println(duplicates.size() + " duplicate record(s) found:");
            for (Person p : duplicates) {
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

    private static void searchAndPrint(String searchType) {
        String searchValue = inputManager.getStringInput("Enter the " + searchType + " to search: ");
        Collection<Person> results = manager.searchPerson(searchValue, searchType);

        System.out.println("\n--- SEARCH RESULTS (" + searchType.toUpperCase() + ") ---");
        if (results.isEmpty()) {
            System.out.println("No records found matching '" + searchValue + "'.");
        } else {
            System.out.println(results.size() + " record(s) found:");
            for (Person person : results) {
                System.out.println(person);
            }
        }
        System.out.println("----------------------------------------");
    }

    private static void quickSearchByEmail() {
        String email = inputManager.getStringInput("Enter the email address to search: ");

        Person found = manager.findPersonByEmail(email);

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
