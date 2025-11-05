package org.example;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Person extends Entry {
    @SerializedName("lastName")
    private String lastName;

    public Person(String name,String lastName, String phonenumber, String email) {
        super(name, phonenumber, email);
        this.lastName = lastName;
    }

    public String getLastName() {return lastName;}

    @Override
    public String getDetails() {
        return "Last Name: " + lastName;
    }
    @Override
    public String toString() {
        return "Name: " + getName() +
                ", Last Name: " + getLastName() +
                ", Email: " + getEmail() +
                ", Phone: " + getPhoneNumber();
    }




}
