package com.haulmont.testtask.DB.ModelDAOs;

import com.haulmont.testtask.DB.DBConnection;
import com.haulmont.testtask.DB.ModelDAOs.ModelDAO;
import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.models.Prescription;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionDAO implements ModelDAO<Prescription> {
    DBConnection connection = new DBConnection();
    @Override
    public List<Prescription> getTable() throws SQLException {
        List<Prescription> prescriptions = new ArrayList<>();
        String query = "SELECT * FROM Prescriptions";
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            Integer id = resultSet.getInt("id");
            String description = resultSet.getString("description");
            Integer patientId = resultSet.getInt("patient");
            Patient patient = new Patient();
            String searchPatientQuery = "SELECT * FROM Patients WHERE Patients.id = " + patientId;
            ResultSet patientResultSet = statement.executeQuery(searchPatientQuery);
            while(patientResultSet.next()){
                int idP = patientResultSet.getInt("id");
                String firstName = patientResultSet.getString("first_name");
                String lastName = patientResultSet.getString("last_name");
                String patronymic = patientResultSet.getString("patronymic");
                String phoneNumber = patientResultSet.getString("phone_number");
                patient = new Patient(idP,firstName,lastName,patronymic,phoneNumber);
            }

            Integer doctorId = resultSet.getInt("doctor");
            Doctor doctor = new Doctor();
            String searchDoctorQuery = "SELECT * FROM Doctors WHERE Doctors.id = " + doctorId;
            ResultSet doctorResultSet = statement.executeQuery(searchDoctorQuery);
            while(doctorResultSet.next()){
                Integer idD = doctorResultSet.getInt("id");
                String firstName = doctorResultSet.getString("first_name");
                String lastName = doctorResultSet.getString("last_name");
                String patronymic = doctorResultSet.getString("patronymic");
                String speciality = doctorResultSet.getString("speciality");
                doctor = new Doctor(idD,firstName,lastName,patronymic,speciality);
            }


            LocalDate creationDate = LocalDate.parse(resultSet.getString("creation_date")) ;
            Integer validityPeriod = resultSet.getInt("validity_period");
            String priority = resultSet.getString("priority");
            Prescription prescription =new Prescription(id,description,patient,doctor,creationDate,validityPeriod,priority);
            prescriptions.add(prescription);
        }
        return prescriptions;
    }

    @Override
    public void setInTable(Prescription prescription) throws SQLException {
        String description = prescription.getDescription();
        int patientID = prescription.getPatient().getId();
        int doctorID = prescription.getDoctor().getId();
        String date = prescription.getCreationDate().toString();
        int validityPeriod = prescription.getValidityPeriod();
        String priority = prescription.getPriority();
        String query = "INSERT INTO Prescriptions (description, patient, doctor, creation_date, validity_period, priority)" +
                "Values" + "('" + description + "'," +
                "'" + patientID + "'," +
                "'" + doctorID + "'," +
                "'" + date + "'," +
                "'" + validityPeriod + "'," +
                "'" + priority + "')";
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
    }

    @Override
    public void updateTable(Prescription prescription) throws SQLException {
        int id = prescription.getId();
        String description = prescription.getDescription();
        int patientID = prescription.getPatient().getId();
        int doctorID = prescription.getDoctor().getId();
        String date = prescription.getCreationDate().toString();
        int validityPeriod = prescription.getValidityPeriod();
        String priority = prescription.getPriority();
        String updateTableQuery = "UPDATE Prescriptions " +
                "SET " + "description = " + "'" + description + "', " + "\n" +
                "patient = " + "'" + patientID + "', " + "\n" +
                "doctor = " + "'" + doctorID + "', " + "\n" +
                "creation_date = " + "'" + date + "', " + "\n" +
                "validity_period = " + "'" + validityPeriod + "', " + "\n" +
                "priority = " + "'" + priority + "' " + "\n" +
                "WHERE id = " + id;
        Statement statement = connection.getConnection().createStatement();
        statement.executeUpdate(updateTableQuery);
    }

    @Override
    public void deleteFromTable(Prescription prescription) throws SQLException {
        String query = "DELETE FROM Prescriptions " +
                "WHERE id = " + prescription.getId();

    }
}
