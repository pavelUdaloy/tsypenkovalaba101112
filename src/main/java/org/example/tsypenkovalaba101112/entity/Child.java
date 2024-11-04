package org.example.tsypenkovalaba101112.entity;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class Child {

    private final Long id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final SimpleObjectProperty<LocalDateTime> birthDay;
    private final StringProperty gender;
    private final StringProperty photo;

    public Child(Long id, String firstName, String lastName, LocalDateTime birthDay, String gender, String photo) {
        this.id = id;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDay = new SimpleObjectProperty<>(birthDay);
        this.gender = new SimpleStringProperty(gender);
        this.photo = new SimpleStringProperty(photo);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public LocalDateTime getBirthDay() {
        return birthDay.get();
    }

    public SimpleObjectProperty<LocalDateTime> birthDayProperty() {
        return birthDay;
    }

    public String getGender() {
        return gender.get();
    }

    public StringProperty genderProperty() {
        return gender;
    }

    public String getPhoto() {
        return photo.get();
    }

    public StringProperty photoProperty() {
        return photo;
    }

    @Override
    public String toString() {
        return id + " " + firstName.get() + ' ' + lastName.get();
    }
}