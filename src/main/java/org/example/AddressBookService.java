package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static org.example.Constants.*;

public class AddressBookService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Map<String, Person> addressBook;
    private final Set<String> usedPhoneNumbers;

    public AddressBookService() {
        this.addressBook = new HashMap<>();
        this.usedPhoneNumbers = new HashSet<>();
    }

    public Person findPersonByEmail(String email) {
        return addressBook.get(email.toLowerCase());
    }

    public OperationResult addPerson(Person newPerson) {

        String key = newPerson.getEmail().toLowerCase();
        String phoneNumber = newPerson.getPhoneNumber();

        OperationResult validationResult = validatePerson(newPerson);
        if (validationResult != null) {
            return validationResult;
        }

        if (usedPhoneNumbers.contains(phoneNumber)) {
            return OperationResult.ERROR_DUPLICATE_PHONE;
        }

        if (addressBook.containsKey(key)) {
            return OperationResult.ERROR_DUPLICATE_EMAIL;
        }

        if (!validateLetters(newPerson.getName())) {
            return OperationResult.ERROR_INVALID_NAME_SURNAME;
        }

        if (!validateLetters(newPerson.getLastName())) {
            return OperationResult.ERROR_INVALID_NAME_SURNAME;
        }

        addressBook.put(key, newPerson);
        usedPhoneNumbers.add(phoneNumber);

        return OperationResult.SUCCESS_ADD;
    }

    private boolean validateLetters(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[\\p{L} ]+$");
    }

    public void listAllPeople() {
        if (addressBook.isEmpty()) {
            System.out.println("The address book is currently empty.");
            return;
        }

        System.out.println("\n--- Address Book List (" + addressBook.size() + " People) ---");

        addressBook.values().stream()
                .sorted(Comparator.comparing(Entry::getName)) // Sort by first name
                .forEach(System.out::println);

        System.out.println("--------------------------------");
    }

    public boolean deletePerson(String email) {
        String key = email.toLowerCase();
        if (addressBook.containsKey(key)) {
            Person removed = addressBook.remove(key);
            usedPhoneNumbers.remove(removed.getPhoneNumber());
            System.out.println("SUCCESS: " + removed.getName() + " has been deleted.");
            return true;
        }
        System.out.println("ERROR: No person found with the email address " + email + ".");
        return false;
    }

    public Collection<Person> searchPerson(String value, String searchType) {
        String lowerValue = value.toLowerCase();
        return addressBook.values().stream()
                .filter(person -> {
                    switch (searchType.toLowerCase()) {
                        case "firstname":
                            return person.getName().toLowerCase().contains(lowerValue);
                        case "lastname":
                            return person.getLastName().toLowerCase().contains(lowerValue);
                        case "phone":
                            return person.getPhoneNumber().contains(value);
                        default:
                            return false;
                    }
                })
                .collect(Collectors.toList());
    }

    public Collection<Person> findDuplicateNames() {
        Map<String, Long> frequencyMap = addressBook.values().stream()
                .collect(Collectors.groupingBy(
                        p -> (p.getName() + p.getLastName()).toLowerCase(),
                        Collectors.counting()
                ));

        return addressBook.values().stream()
                .filter(p -> {
                    String key = (p.getName() + p.getLastName()).toLowerCase();
                    return frequencyMap.getOrDefault(key, 0L) > 1;
                })
                .collect(Collectors.toList());
    }

    public String convertBookToJson() {
        return gson.toJson(addressBook.values());
    }

    public boolean saveData() {
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            gson.toJson(addressBook, writer);
            System.out.println("SUCCESS: Address book data has been saved to file.");
            return true;
        } catch (IOException e) {
            System.err.println("ERROR: An error occurred while saving data. Details: " + e.getMessage());
            return false;
        }
    }

    public boolean loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            System.out.println("INFO: Record file not found. A new empty address book has been created.");
            return false;
        }

        try (FileReader reader = new FileReader(FILE_PATH)) {
            Type mapType = new TypeToken<HashMap<String, Person>>() {}.getType();
            Map<String, Person> loadedBook = gson.fromJson(reader, mapType);

            if (loadedBook != null) {
                this.addressBook = loadedBook;
                this.usedPhoneNumbers.clear();
                for (Person person : addressBook.values()) {
                    this.usedPhoneNumbers.add(person.getPhoneNumber());
                }
                System.out.println("SUCCESS: " + addressBook.size() + " people loaded into the address book.");
                return true;
            } else {
                System.out.println("INFO: Record file is empty or unreadable. Using a new empty address book.");
                return false;
            }

        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: A read/format error occurred while loading data. The file may be corrupted. Details: " + e.getMessage());
            return false;
        }
    }

    private OperationResult validatePerson(Person person) {
        String email = person.getEmail();
        String phone = person.getPhoneNumber();

        if (validateEmail(email)) {
            return OperationResult.ERROR_INVALID_EMAIL;
        }

        if (validatePhoneLength(phone)) {
            return OperationResult.ERROR_INVALID_PHONE;
        }

        return null;
    }

    private boolean validateEmail(String email) {
        if (email == null || (email = email.trim()).isEmpty()) {
            return false;
        }
        String lowerEmail = email.toLowerCase();

        for (String domain : VALID_EMAIL_DOMAINS) {
            if (lowerEmail.endsWith(domain)) {
                return true;
            }
        }
        return false;
    }

    private boolean validatePhoneLength(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        String cleaned = phoneNumber.replaceAll("[^0-9]", "");
        return cleaned.length() == VALID_PHONE_LENGTH;
    }
}
