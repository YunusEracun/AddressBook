package org.example.util;

public final class Constants {

    // Prevent instantiation of this class
    private Constants() {}

    // --- RESTRICTIONS AND SETTINGS ---
    public static final int VALID_PHONE_LENGTH = 10;

    public static final String[] VALID_EMAIL_DOMAINS =
            { "@hotmail.com", "@gmail.com", "@outlook.com" };

    public static final String FILE_PATH = "address_book.json";

    // --- RESULT MESSAGES ---
    public static final String ERROR_DUPLICATE_EMAIL =
            "ERROR: This email address (%s) is already registered.";

    public static final String ERROR_DUPLICATE_PHONE =
            "ERROR: This phone number (%s) is already registered.";

    public static final String ERROR_INVALID_EMAIL =
            "ERROR: The email address domain is invalid. Use domains like (%s).";

    public static final String ERROR_INVALID_PHONE =
            "ERROR: The phone number must be " + VALID_PHONE_LENGTH + " digits long.";

    public static final String SUCCESS_ADD =
            "SUCCESS: %s has been added to the address book.";

    public static final String SUCCESS_DELETE =
            "SUCCESS: %s has been deleted.";

    public static final String INFO_NO_FILE_FOUND =
            "INFO: No record file found. A new empty address book has been created.";

    public static final String INFO_NO_MATCH_FOUND =
            "INFO: No records found matching the search criteria.";

    public static final String ERROR_DURING_SAVE =
            "ERROR: An error occurred while saving data. Details: %s";

    public static final String ERROR_INVALID_NAME_SURNAME =
            "ERROR: First name and last name must contain only letters.";
}
