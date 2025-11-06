package org.example;
import com.google.gson.annotations.SerializedName;
import java.util.Objects;

public class Person extends Entry {
    @SerializedName("lastName")
    private String lastName;

    public Person(String name,String email ,String phoneNumber, String lastName) {
        super(name, email, phoneNumber);
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
