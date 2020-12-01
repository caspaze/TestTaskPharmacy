package com.haulmont.testtask.DB;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Класс, инициализирующий базу данных. Производится создание таблиц и заполнение их тестовыми данными
 */
public class InitDB {

    private final String dropPatientTable = "DROP TABLE IF EXISTS Patients";
    private final String dropDoctorsTable = "DROP TABLE IF EXISTS Doctors";
    private final String dropPrescriptionsTable = "DROP TABLE IF EXISTS Prescriptions";
    private final String createTablesQuery = "CREATE TABLE Patients (\n" +
            "   id INT IDENTITY NOT NULL,\n" +
            "   first_name VARCHAR(50) NOT NULL,\n" +
            "   last_name VARCHAR(20) NOT NULL,\n" +
            "   patronymic VARCHAR(20) NOT NULL,\n" +
            "   phone_number VARCHAR(11),\n" +
            "   PRIMARY KEY (id) \n" +
            ");" + "\n" +
            "CREATE TABLE Doctors (\n" +
            "   id INT IDENTITY NOT NULL,\n" +
            "   first_name VARCHAR(50) NOT NULL,\n" +
            "   last_name VARCHAR(20) NOT NULL,\n" +
            "   patronymic VARCHAR(20) NOT NULL,\n" +
            "   speciality VARCHAR(20),\n" +
            "   PRIMARY KEY (id) \n" +
            ");" + "\n" +
            "CREATE TABLE Prescriptions (\n" +
            "   id INT IDENTITY NOT NULL,\n" +
            "   description VARCHAR(200) NOT NULL,\n" +
            "   patient INT,\n" +
            "   doctor INT,\n" +
            "   creation_date DATE NOT NULL,\n" +
            "   validity_period INT,\n" +
            "   priority VARCHAR(10),\n" +
            "   PRIMARY KEY (id) \n" +
            ");";
    private final String addPatientForeignKey = "ALTER TABLE Prescriptions ADD FOREIGN KEY (patient) REFERENCES Patients (id);";
    private final String addDoctorsForeignKey = "ALTER TABLE Prescriptions ADD FOREIGN KEY (doctor) REFERENCES Doctors (id);";

    private String insertDataQuery = "INSERT INTO Patients (first_name, last_name, patronymic, phone_number)" +
            "   VALUES('Ivan','Ivanov','Ivanovich','987590')" +
            "INSERT INTO Patients (first_name, last_name, patronymic, phone_number)" +
            "   VALUES('Igor','Lukin','Andreevich','543563')" +
            "INSERT INTO Patients (first_name, last_name, patronymic, phone_number)" +
            "   VALUES('Alexey','Korchagin','Sergeevich','253423')" +
            "INSERT INTO Patients (first_name, last_name, patronymic, phone_number)" +
            "   VALUES('Konstantin','Osipov','Petrovich','254322')" +
            "INSERT INTO Patients (first_name, last_name, patronymic, phone_number)" +
            "   VALUES('Igor','Gulenko','Alexandrovich','349589')" +
            "INSERT INTO Doctors (first_name, last_name, patronymic, speciality)" +
            "   VALUES('Vadim','Novoselov','Garikovich','psychiatrist')" +
            "INSERT INTO Doctors (first_name, last_name, patronymic, speciality)" +
            "   VALUES('Alexandr','Baklashov','Ivanovich','surgeon')" +
            "INSERT INTO Doctors (first_name, last_name, patronymic, speciality)" +
            "   VALUES('Mariya','Koroleva','Evgenevna','pediatrician')" +
            "INSERT INTO Prescriptions (description, patient, doctor, creation_date, validity_period, priority)" +
            "   VALUES('Get some rest', 2, 1, '2020-11-19', 3, 'Normal')" +
            "INSERT INTO Prescriptions (description, patient, doctor, creation_date, validity_period, priority)" +
            "   VALUES('Get pills', 3, 0, '2020-12-01', 10, 'Cito')" +
            "INSERT INTO Prescriptions (description, patient, doctor, creation_date, validity_period, priority)" +
            "   VALUES('Heart transplant', 4, 2, '2019-10-03', 1, 'Statim')" +
            "INSERT INTO Prescriptions (description, patient, doctor, creation_date, validity_period, priority)" +
            "   VALUES('Sleep more', 2, 0, '2018-10-03', 36, 'Normal')";


    DBConnection connection = new DBConnection();
    Statement statement = connection.getConnection().createStatement();


    public InitDB() throws SQLException {
        statement.execute(dropPrescriptionsTable);
        statement.execute(dropPatientTable);
        statement.execute(dropDoctorsTable);
        statement.execute(createTablesQuery);
        statement.execute(addPatientForeignKey);
        statement.execute(addDoctorsForeignKey);
        statement.execute(insertDataQuery);
    }
}
