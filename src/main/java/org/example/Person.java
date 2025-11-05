package org.example;

import java.util.Objects;

public class Person {

    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;

    public Person(String firstName, String lastName, String phoneNumber, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public String getPhoneNumber() {
        return phoneNumber;
    }
    public String getEmail() {
        return email;
    }

    @Override
    // Overridden to provide a readable string representation instead of something like org.example.Person@7a81197d
    public String toString() {
        return "First Name: " + firstName +
                ", Last Name: " + lastName +
                ", Phone: " + phoneNumber +
                ", Email: " + email;
    }

    @Override
    // By default, equals compares memory addresses; overridden to compare by content (email)
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(email, person.email);
    }

    @Override
    // The person's hash value is based on their email address
    public int hashCode() {
        return Objects.hash(email);
    }
}
