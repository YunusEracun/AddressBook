package org.example.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.example.util.OperationResult;
import org.example.model.Company;
import org.example.model.Entry;
import org.example.model.Person;

import java.util.*;
import java.io.*;
import java.lang.reflect.Type;
import java.util.stream.Collectors;

import static org.example.util.Constants.FILE_PATH;
import static org.example.util.Constants.VALID_EMAIL_DOMAINS;
import static org.example.util.Constants.VALID_PHONE_LENGTH;

public class AddressBookService {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private Map<String, Entry> addressBook;
    private final Set<String> usedPhoneNumbers;

    public AddressBookService() {
        this.addressBook = new HashMap<>();
        this.usedPhoneNumbers = new HashSet<>();
    }

    public Entry findEntryByEmail(String email) {
        return addressBook.get(email.toLowerCase());
    }

    public OperationResult addEntry(Entry newEntry) {

        String key = newEntry.getEmail().toLowerCase();
        String phoneNumber = newEntry.getPhoneNumber();

        OperationResult validationResult = validateEntry(newEntry);
        if (validationResult != null) {
            return validationResult;
        }

        if (usedPhoneNumbers.contains(phoneNumber)) {
            return OperationResult.ERROR_DUPLICATE_PHONE;
        }

        if (addressBook.containsKey(key)) {
            return OperationResult.ERROR_DUPLICATE_EMAIL;
        }

        addressBook.put(key, newEntry);
        usedPhoneNumbers.add(phoneNumber);

        return OperationResult.SUCCESS_ADD;
    }

    private boolean validateLetters(String text) {
        if (text == null || text.trim().isEmpty()) {
            return false;
        }
        return text.matches("^[\\p{L} ]+$");
    }

    public void listAllEntries() {
        if (addressBook.isEmpty()) {
            System.out.println("The address book is currently empty.");
            return;
        }

        System.out.println("\n--- Address Book List (" + addressBook.size() + " Entries ---");

        addressBook.values().stream()
                .sorted(Comparator.comparing(Entry::getName)) // Sort by first name
                .forEach(System.out::println);

        System.out.println("--------------------------------");
    }

    public boolean deleteEntry(String email) {
        String key = email.toLowerCase();
        if (addressBook.containsKey(key)) {
            Entry removed = addressBook.remove(key);
            usedPhoneNumbers.remove(removed.getPhoneNumber());
            System.out.println("SUCCESS: " + removed.getName() + " has been deleted.");
            return true;
        }
        System.out.println("ERROR: No person found with the email address " + email + ".");
        return false;
    }

    public Collection<Entry> searchEntries(String entryType, String searchField, String searchValue) {
        String lowerSearchValue = searchValue.toLowerCase();

        var stream = addressBook.values().stream();

        if (entryType.equalsIgnoreCase("Person")) {
            stream = stream.filter(entry -> entry instanceof Person);
        } else if (entryType.equalsIgnoreCase("Company")) {
            stream = stream.filter(entry -> entry instanceof Company);
        }

        return stream.filter(entry -> {
            switch (searchField.trim().toLowerCase()) {
                case "name":
                    return entry.getName().toLowerCase().contains(lowerSearchValue);
                case "phone":
                    return entry.getPhoneNumber().contains(searchValue);
                case "lastName":
                    if (entry instanceof Person person) {
                        return person.getLastName().toLowerCase().contains(lowerSearchValue);
                    }
                    return false;
                case "taxnumber":
                    if (entry instanceof Company company) {
                        return company.getTaxNumber().contains(lowerSearchValue);
                    }
                    return false;
                case "address":
                    if (entry instanceof Company company) {
                        return company.getAddress().contains(lowerSearchValue);
                    }
                    return false;
                    default:
                        return false;
            }
        }).collect(Collectors.toList());

    }

    public Collection<Entry> findDuplicateNames() {
        Map<String, Long> frequencyMap = addressBook.values().stream()
                .collect(Collectors.groupingBy(
                        p -> (p.getName().toLowerCase()),
                        Collectors.counting()
                ));

        return addressBook.values().stream()
                .filter(entry -> {
                    String key = entry.getName().toLowerCase();
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
                // Yüklenenleri 'Entry' Map'ine aktarıyoruz
                this.addressBook = new HashMap<>(loadedBook);

                this.usedPhoneNumbers.clear();
                for (Entry entry : addressBook.values()) {
                    this.usedPhoneNumbers.add(entry.getPhoneNumber());
                }
                System.out.println("SUCCESS: " + addressBook.size() + " entries loaded into the address book.");
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

    private OperationResult validateEntry(Entry entry) {
        String email = entry.getEmail();
        String phone = entry.getPhoneNumber();

        if (!validateEmail(email)) {
            return OperationResult.ERROR_INVALID_EMAIL;
        }

        if (!validatePhoneLength(phone)) {
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
