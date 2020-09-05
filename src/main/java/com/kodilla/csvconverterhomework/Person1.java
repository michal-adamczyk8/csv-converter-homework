package com.kodilla.csvconverterhomework;

import java.time.LocalDate;
import java.util.Date;

public class Person1 {
    private String firstName;
    private String lastName;
    private LocalDate birth;

    public Person1() {};

    public Person1(String firstName, String lastName, String birth) {
        this.firstName = firstName;
        this.lastName = lastName;
        String[] data = birth.split("-");
        this.birth = LocalDate.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        String[] data = birth.split("-");
        this.birth = LocalDate.of(Integer.parseInt(data[0]), Integer.parseInt(data[1]), Integer.parseInt(data[2]));
    }
}
