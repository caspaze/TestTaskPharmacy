package com.haulmont.testtask.DB.ModelDAOs;

import com.haulmont.testtask.DB.DBConnection;
import com.haulmont.testtask.DB.ModelDAOs.ModelDAO;
import com.haulmont.testtask.models.Doctor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DoctorDAO implements ModelDAO<Doctor> {
    DBConnection connection = new DBConnection();
    @Override
    public List<Doctor> getTable() throws SQLException {
        List<Doctor> doctors = new ArrayList<>();
        String query = "SELECT * FROM doctors";
        Statement statement = connection.getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            String patronymic = resultSet.getString("patronymic");
            String speciality = resultSet.getString("speciality");
            doctors.add(new Doctor(id,firstName,lastName,patronymic,speciality));
        }
        return doctors;
    }

    @Override
    public void setInTable(Doctor doctor) throws SQLException {
        String firstName = doctor.getFirstName();
        String lastName = doctor.getLastName();
        String patronymic = doctor.getPatronymic();
        String speciality = doctor.getSpeciality();
        String query = "INSERT INTO doctors (first_name, last_name, patronymic, speciality)" +
                "Values" + "('" + firstName + "'," + "'" + lastName + "'," + "'" + patronymic + "', '" + speciality + "')";
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
    }

    @Override
    public void updateTable(Doctor doctor) throws SQLException {
        int id = doctor.getId();
        String firstName = doctor.getFirstName();
        String lastName = doctor.getLastName();
        String patronymic = doctor.getPatronymic();
        String speciality = doctor.getSpeciality();
        String updateTableQuery = "UPDATE Doctors " +
                "SET " + "first_name = " + "'" + firstName + "', " + "\n" +
                "last_name = " + "'" + lastName + "', " + "\n" +
                "patronymic = " + "'" + patronymic + "', " + "\n" +
                "speciality = " + "'" + speciality + "' " + "\n" +
                "WHERE id = " + id;

        Statement statement = connection.getConnection().createStatement();
        statement.executeUpdate(updateTableQuery);
    }

    @Override
    public void deleteFromTable(Doctor doctor) throws SQLException {
        String query = "DELETE FROM doctors " +
                "WHERE id = " + doctor.getId();
        Statement statement = connection.getConnection().createStatement();
        statement.execute(query);
    }
    public List<Integer> getPrescriptionStat(List<Doctor> doctors) throws SQLException{
        List<Integer> prescriptionStat = new ArrayList<>();
        Statement statement = connection.getConnection().createStatement();
        for(Doctor d:doctors){
            String query = "SELECT COUNT(*) as number " +
                    "FROM Prescriptions LEFT JOIN Doctors on Prescriptions.doctor = Doctors.id " +
                    "WHERE Doctors.id = " + d.getId();
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()){
                int number = resultSet.getInt("number");
                prescriptionStat.add(number);
            }
        }
        return prescriptionStat;
    }

}
