package com.haulmont.testtask.models;

import java.time.LocalDate;

/**
 * Сущностный класс рецепт
 */
public class Prescription {
    private Integer id;
    private String description;
    private Patient patient;
    private Doctor doctor;
    private LocalDate creationDate;
    private Integer validityPeriod;
    private String priority;

    @Override
    public String toString() {
        return "Recipe{" +
                "description='" + description + '\'' +
                ", patient=" + patient +
                ", doctor=" + doctor +
                ", creationDate=" + creationDate +
                ", validityPeriod=" + validityPeriod +
                ", priority='" + priority + '\'' +
                '}';
    }
    public Prescription(){

    }

    public Prescription(Integer id, String description, Patient patient, Doctor doctor, LocalDate creationDate, Integer validityPeriod, String priority) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = creationDate;
        this.validityPeriod = validityPeriod;
        this.priority = priority;
    }

    public Prescription(String description, Patient patient, Doctor doctor, LocalDate creationDate, Integer validityPeriod, String priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.creationDate = creationDate;
        this.validityPeriod = validityPeriod;
        this.priority = priority;
    }
    public String getDoctorFIO(){
        return doctor.getFirstName() + " " + doctor.getLastName() + " " + doctor.getPatronymic();
    }
    public String getPatientFIO(){
        return patient.getFirstName() + " " + patient.getLastName() + " " + patient.getPatronymic();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Integer getValidityPeriod() {
        return validityPeriod;
    }

    public void setValidityPeriod(Integer validityPeriod) {
        this.validityPeriod = validityPeriod;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
