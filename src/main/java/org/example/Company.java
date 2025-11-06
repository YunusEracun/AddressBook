package org.example;

public class Company extends Entry {
    private String taxNumber;
    private String address;

    public Company(String name, String email, String phoneNumber, String address, String taxNumber ) {
        super(name, email, phoneNumber);
        this.taxNumber = taxNumber;
        this.address = address;

    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public String getAddress() {
        return address;
    }

    @Override
    public String getDetails() {
        return "Tax Number: " + taxNumber + ", Address: " + address;
    }

    @Override
    public String toString() {
        return "Company Name: " + getName() +
                ", Tax Number: " + taxNumber +
                ", Address: " + address +
                ", Email: " + getEmail() +
                ", Phone: " + getPhoneNumber();
    }


}
