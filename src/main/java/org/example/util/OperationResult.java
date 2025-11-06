package org.example.util;

// Represents all possible result types (success/error) returned by the Service layer to the UI layer.
public enum OperationResult {

    // SUCCESS MESSAGES
    SUCCESS_ADD,
    SUCCESS_DELETE,

    INFO_NO_FILE_FOUND,
    INFO_NO_RECORD_FOUND,

    // ERROR MESSAGES
    ERROR_DUPLICATE_EMAIL,
    ERROR_DUPLICATE_PHONE,
    ERROR_INVALID_EMAIL,
    ERROR_INVALID_PHONE,
    ERROR_DURING_SAVE,
    ERROR_INVALID_NAME_SURNAME,
}
