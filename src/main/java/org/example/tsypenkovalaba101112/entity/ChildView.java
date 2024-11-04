package org.example.tsypenkovalaba101112.entity;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.LocalDateTime;

public class ChildView {

    private final Long id;
    private final StringProperty firstName;
    private final StringProperty lastName;
    private final SimpleObjectProperty<LocalDateTime> birthDay;
    private final StringProperty gender;
    private final StringProperty photo;
    private final StringProperty parentFatherName;
    private final StringProperty parentMotherName;

    public ChildView(Long id, String firstName, String lastName, LocalDateTime birthDay, String gender, String photo, String parentFatherName, String parentMotherName) {
        this.id = id;
        this.firstName = new SimpleStringProperty(firstName);
        this.lastName = new SimpleStringProperty(lastName);
        this.birthDay = new SimpleObjectProperty<>(birthDay);
        this.gender = new SimpleStringProperty(gender);
        this.photo = new SimpleStringProperty(photo);
        this.parentFatherName = new SimpleStringProperty(parentFatherName);
        this.parentMotherName = new SimpleStringProperty(parentMotherName);
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

    public String getParentFatherName() {
        return parentFatherName.get();
    }

    public StringProperty parentFatherNameProperty() {
        return parentFatherName;
    }

    public String getParentMotherName() {
        return parentMotherName.get();
    }

    public StringProperty parentMotherNameProperty() {
        return parentMotherName;
    }

    @Override
    public String toString() {
        return id + " " + firstName.get() + ' ' + lastName.get();
    }
}
