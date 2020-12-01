package com.haulmont.testtask.DB.ModelDAOs;

import com.haulmont.testtask.DB.DBConnection;
import com.haulmont.testtask.DB.ModelDAOs.ModelDAO;
import com.haulmont.testtask.models.Patient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PatientDAO implements ModelDAO<Patient> {


    public PatientDAO() {
    }

    @Override
    public List<Patient> getTable() throws SQLException {
        List<Patient> patients = new ArrayList<>();
        String query = "SELECT * FROM patients";
        DBConnection connection = new DBConnection();
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            Integer id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            String phoneNumber = resultSet.getString("phone_number");
            patients.add(new Patient(id,firstName,lastName,patronymic,phoneNumber));
        }
        connection.getConnection().close();
        return patients;
    }
    @Override
    public void setInTable(Patient patient) throws SQLException {
        DBConnection connection = new DBConnection();
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        String patronymic = patient.getPatronymic();
        String phoneNumber = patient.getPhoneNumber();
        String query = "INSERT INTO Patients (first_name, last_name, patronymic, phone_number) " +
                "Values" + "('" + firstName + "'," + "'" + lastName + "'," + "'" + patronymic + "'," + phoneNumber + ")";
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
        connection.getConnection().close();
    }
    public void updateTable(Patient patient) throws SQLException{
        DBConnection connection = new DBConnection();
        int id = patient.getId();
        String firstName = patient.getFirstName();
        String lastName = patient.getLastName();
        String patronymic = patient.getPatronymic();
        String phoneNumber = patient.getPhoneNumber();
        String updateTableQuery = "UPDATE Patients " +
                "SET " + "first_name = " + "'" + firstName + "', " + "\n" +
                "last_name = " + "'" + lastName + "', " + "\n" +
                "patronymic = " + "'" + patronymic + "', " + "\n" +
                "phone_number = " + "'" + phoneNumber + "' " + "\n" +
                "WHERE id = " + id;

        Statement statement = connection.getConnection().createStatement();
        statement.executeUpdate(updateTableQuery);
        connection.getConnection().close();
    }

    @Override
    public void deleteFromTable(Patient patient) throws SQLException {
        DBConnection connection = new DBConnection();
        String query = "DELETE FROM patients " +
                "WHERE id = " + patient.getId();
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
        connection.getConnection().close();
    }
}
