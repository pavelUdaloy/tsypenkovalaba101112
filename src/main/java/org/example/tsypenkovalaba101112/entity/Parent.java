package org.example.tsypenkovalaba101112.entity;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Parent {

    private final Long id;
    private final StringProperty motherFirstName;
    private final StringProperty fatherFirstName;
    private final StringProperty lastName;

    public Parent(Long id, String motherFirstName, String fatherFirstName, String lastName) {
        this.id = id;
        this.motherFirstName = new SimpleStringProperty(motherFirstName);
        this.fatherFirstName = new SimpleStringProperty(fatherFirstName);
        this.lastName = new SimpleStringProperty(lastName);
    }

    public Parent(Long id, String lastName) {
        this.id = id;
        this.fatherFirstName = null;
        this.motherFirstName = null;
        this.lastName = new SimpleStringProperty(lastName);
    }

    public Long getId() {
        return id;
    }

    public String getMotherFirstName() {
        return motherFirstName.get();
    }

    public StringProperty motherFirstNameProperty() {
        return motherFirstName;
    }

    public String getFatherFirstName() {
        return fatherFirstName.get();
    }

    public StringProperty fatherFirstNameProperty() {
        return fatherFirstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    @Override
    public String toString() {
        String motherName = motherFirstName != null ? motherFirstName.get() : "не указано";
        String fatherName = fatherFirstName != null ? fatherFirstName.get() : "не указано";
        String lastNameValue = lastName.get() != null ? lastName.get() : "не указано";

        return id + " " + lastNameValue;
    }
}
