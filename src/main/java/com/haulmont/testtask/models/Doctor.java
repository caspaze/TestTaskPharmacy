package com.haulmont.testtask.models;

import java.util.Objects;

/**
 * Сущностный класс доктор
 */
public class Doctor {
    private Integer id;
    private String firstName;
    private String lastName;
    private String patronymic;
    private String speciality;

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }

    public Doctor(String firstName, String lastName, String patronymic, String speciality) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Doctor(){

    }
    public Doctor(Integer id, String firstName, String lastName, String patronymic, String speciality) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.patronymic = patronymic;
        this.speciality = speciality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return id.equals(doctor.id) &&
                firstName.equals(doctor.firstName) &&
                lastName.equals(doctor.lastName) &&
                patronymic.equals(doctor.patronymic) &&
                speciality.equals(doctor.speciality);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, patronymic, speciality);
    }

    public String getDoctorData(){
        return firstName + " " + lastName + " " + patronymic + " , " + speciality;
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

    public String getPatronymic() {
        return patronymic;
    }

    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }
}
