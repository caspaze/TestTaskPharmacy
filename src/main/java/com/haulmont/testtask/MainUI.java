package com.haulmont.testtask;



import com.haulmont.testtask.DB.ModelDAOs.DoctorDAO;
import com.haulmont.testtask.DB.InitDB;
import com.haulmont.testtask.DB.ModelDAOs.PatientDAO;
import com.haulmont.testtask.DB.ModelDAOs.PrescriptionDAO;
import com.haulmont.testtask.models.Doctor;
import com.haulmont.testtask.models.Patient;
import com.haulmont.testtask.models.Prescription;
import com.vaadin.annotations.Theme;
import com.vaadin.data.HasValue;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Theme(ValoTheme.THEME_NAME)
public class MainUI extends UI {
    /**
     * Layout на котором располагаются все элемента пользовательского интерфейса
     */
    AbsoluteLayout absoluteLayout = new AbsoluteLayout();
    /**
     * Объект интерфейся ModelDAO для взаимодействия класса Patient с базой данных
     */
    PatientDAO patientDAO = new PatientDAO();
    /**
     * Объект интерфейся ModelDAO для взаимодействия класса Doctor с базой данных
     */
    DoctorDAO doctorDAO = new DoctorDAO();
    /**
     * Объект интерфейся ModelDAO для взаимодействия класса Prescription с базой данных
     */
    PrescriptionDAO prescriptionDAO = new PrescriptionDAO();

    /**
     * Список всех пациентов
     */
    List<Patient> patients = new ArrayList<>();
    /**
     * Таблица, отобращающая данные о пациентах
     */
    Grid<Patient> patientsGrid = new Grid<>();
    /**
     * Список кнопок для редактирования пациентов
     */
    List<Button> editPatientButtons = new ArrayList<>();
    /**
     * Список кнопок для удаления пациентов
     */
    List<Button> deletePatientButtons = new ArrayList<>();
    /**
     * Кнопка добавления пациента
     */
    Button addPatientButton = new Button("Добавить пациента");

    /**
     * Список докторов
     */
    List<Doctor> doctors = new ArrayList<>();
    /**
     * Список, хранящий количество выписанных каждым доктором рецептов
     */
    List<Integer> prescriptionsStat = new ArrayList<>();
    /**
     * Таблица, отобращающая данные о докторах
     */
    Grid<Doctor> doctorsGrid = new Grid<>();
    /**
     * Таблица, отобращающая статистику о выписанных рецептах
     */
    Grid<Integer> doctorPrescriptionsStatsGrid = new Grid<>();
    /**
     * Список кнопок для редактирования докторов
     */
    List<Button> editDoctorButtons = new ArrayList<>();
    /**
     * Список кнопок для удаления докторов
     */
    List<Button> deleteDoctorButtons = new ArrayList<>();
    /**
     * Кнопка добавления нового доктора
     */
    Button addDoctorButton = new Button("Добавить доктора");

    /**
     * Список рецептов
     */
    List<Prescription> prescriptions = new ArrayList<>();
    /**
     * Таблица, отобращающая данные о рецептах
     */
    Grid<Prescription> prescriptionsGrid = new Grid<>();
    /**
     * Список кнопок для редактирования рецептов
     */
    List<Button> editPrescriptionsButtons = new ArrayList<>();
    /**
     * Список кнопок для удаления рецептов
     */
    List<Button> deletePrescriptionsButtons = new ArrayList<>();
    /**
     * Кнопка добавления нового рецепта
     */
    Button addPrescriptionButton = new Button("Добавить рецепет");

    /**
     * Функция инициализации кнопки добавления нового пациента
     */
    private void initAddPatientButton(){
        //Добавления обработчика нажатия
        addPatientButton.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                //При нажатии создается новое окно с 4 полями для ввода данных о новом пациенте
                Window addPatientWindow = new Window();
                addPatientWindow.setWidth("500");
                addPatientWindow.center();
                VerticalLayout subLayout = new VerticalLayout();
                addPatientWindow.setContent(subLayout);

                FormLayout form = new FormLayout();
                TextField tf1 = new TextField("Имя");
                TextField tf2 = new TextField("Фамилия");
                TextField tf3 = new TextField("Отчество");
                TextField tf4 = new TextField("Номер телефона");
                //Все поля являются обязательными к заполнению
                tf1.setRequiredIndicatorVisible(true);
                tf2.setRequiredIndicatorVisible(true);
                tf3.setRequiredIndicatorVisible(true);
                tf4.setRequiredIndicatorVisible(true);
                form.addComponent(tf1);
                form.addComponent(tf2);
                form.addComponent(tf3);
                form.addComponent(tf4);
                subLayout.addComponent(form);

                Button confirmButton = new Button("ОК");
                Button cancelButton = new Button("Отмена");
                subLayout.addComponent(confirmButton);
                subLayout.addComponent(cancelButton);
                addWindow(addPatientWindow);
                //Обработчик нажатия кнопки "ОК"
                confirmButton.addClickListener((Button.ClickListener) clickEvent12 -> {
                    //Создается новый пациент по данным с текстовых полей (поле id присваивается базой данных)
                    Patient newPatient = new Patient(tf1.getValue(),tf2.getValue(),tf3.getValue(),tf4.getValue());
                    try {
                        //Новый пациент добавляется в базу данных
                        patientDAO.setInTable(newPatient);
                        //Сразу после добавления пациента в бд и присвоения ему там id, обновляется список всех пациентов
                        patients = patientDAO.getTable();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    //Список с новым пациентам помещается в таблицу вместо старого
                    patientsGrid.setItems(patients);
                    //Удаляются предыдущие кнопки редактирования и удаления пациентов и создаются новые
                    for(Button b:editPatientButtons){
                        absoluteLayout.removeComponent(b);
                    }
                    for(Button b:deletePatientButtons){
                        absoluteLayout.removeComponent(b);
                    }
                    createEditPatientButtons();
                    createDeletePatientButtons();
                    addPatientWindow.close();
                });
                //Обработчик кнопки "Отмена". Закрывает окно добавления пациента при нажатии
                cancelButton.addClickListener((Button.ClickListener) clickEvent1 -> addPatientWindow.close());
            }
        });
    }

    /**
     *  Функция создания кнопок редактирования пациентов
     */
    private void createEditPatientButtons(){
        //Для каждого пациента создается кнопка редактирования
        for(int i =0;i<patients.size();i++){
            Button button = new Button(VaadinIcons.EDIT);
            button.setHeight("39");
            int top = 88 + 38*i;
            int finalI = i;
            //Добавление кнопки в спок кнопок редактирования пациентов
            editPatientButtons.add(button);
            //Обработчик нажатия кнопки редактирования
            button.addClickListener((Button.ClickListener) clickEvent -> {
                //Создание нового окна для редактирования пациента
                Window editPatientWindow = new Window();
                editPatientWindow.setWidth("500");
                editPatientWindow.center();
                VerticalLayout subLayout = new VerticalLayout();
                editPatientWindow.setContent(subLayout);

                Patient patient = patients.get(finalI);
                Patient editedPatient = new Patient();
                editedPatient.setId(patient.getId());

                FormLayout form = new FormLayout();
                //Поля для редактирвоания. Изначально инициализированны текущими значениями данных пациента
                TextField tf1 = new TextField("Имя",patient.getFirstName());
                TextField tf2 = new TextField("Фамилия",patient.getLastName());
                TextField tf3 = new TextField("Отчество",patient.getPatronymic());
                TextField tf4 = new TextField("Номер телефона",patient.getPhoneNumber());
                Button confirmButton = new Button("ОК");
                Button cancelButton = new Button("Отмена");
                confirmButton.setEnabled(false);

                tf1.setRequiredIndicatorVisible(true);
                tf2.setRequiredIndicatorVisible(true);
                tf3.setRequiredIndicatorVisible(true);
                tf4.setRequiredIndicatorVisible(true);
                /*
                    Серия датчиков изменения состояния для каждого редактируемого поля.
                    Если значения всех полей после редактировния не изменились, то кнопка добавления не будет доступна
                 */
                tf1.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf1.getValue().equals(patient.getFirstName())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf2.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf2.getValue().equals(patient.getLastName())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf3.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf3.getValue().equals(patient.getPatronymic())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf4.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf4.getValue().equals(patient.getPhoneNumber())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                //Обработчик нажатия кнопки "Ок"
                //Создается новый пациент на основании введенных данных и добавляется в таблицу вместо предыдущего
                confirmButton.addClickListener((Button.ClickListener) clickEvent1 -> {
                    editedPatient.setFirstName(tf1.getValue());
                    editedPatient.setLastName(tf2.getValue());
                    editedPatient.setPatronymic(tf3.getValue());
                    editedPatient.setPhoneNumber(tf4.getValue());
                    patients.set(finalI,editedPatient);
                    try{
                        patientDAO.updateTable(editedPatient);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    patientsGrid.setItems(patients);

                    editPatientWindow.close();
                });
                cancelButton.addClickListener((Button.ClickListener) clickEvent12 -> editPatientWindow.close());

                form.addComponent(tf1);
                form.addComponent(tf2);
                form.addComponent(tf3);
                form.addComponent(tf4);
                subLayout.addComponent(form);
                subLayout.addComponent(confirmButton);
                subLayout.addComponent(cancelButton);
                addWindow(editPatientWindow);
            });
            absoluteLayout.addComponent(button,"left:500px;" + "top:" + top + "px");
        }
    }

    /**
     * Функция создания кнопок удаления пациентов
     */
    private void createDeletePatientButtons(){
        //Для каждого пациента создается кнопка удаления
        for(int i =0;i<patients.size();i++) {
            Button button = new Button(VaadinIcons.DEL);
            button.setHeight("39");
            deletePatientButtons.add(button);
            int top = 88 + 38 * i;
            int finalI = i;
            button.addClickListener((Button.ClickListener) clickEvent -> {
                //Берется соответствующий пациент из списка пациентов и удаляется из базы данных и списка пациентов
                Patient deletedPatient = patients.get(finalI);
                try {
                    patientDAO.deleteFromTable(deletedPatient);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                patients.remove(finalI);
                //В таблицу помещается новый список пациентов
                patientsGrid.setItems(patients);
                //Удаляются кнопки редактирования и удаления пациентов и создаются новые
                for(Button b:editPatientButtons){
                    absoluteLayout.removeComponent(b);
                }
                for(Button b:deletePatientButtons){
                    absoluteLayout.removeComponent(b);
                }
                createEditPatientButtons();
                createDeletePatientButtons();
            });
            absoluteLayout.addComponent(button,"left:550px;" + "top:" + top + "px");
        }
    }
    /**
     * Функция инициализации кнопки добавления нового доктора.
     * По своей логике аналогична функции инициализации кнопки добавления пациента
     */
    private void initAddDoctorButton(){
        addDoctorButton.addClickListener((Button.ClickListener) clickEvent -> {
            Window addDoctorWindow = new Window();
            addDoctorWindow.setWidth("500");
            addDoctorWindow.center();
            VerticalLayout subLayout = new VerticalLayout();
            addDoctorWindow.setContent(subLayout);

            FormLayout form = new FormLayout();
            TextField tf1 = new TextField("Имя");
            TextField tf2 = new TextField("Фамилия");
            TextField tf3 = new TextField("Отчество");
            TextField tf4 = new TextField("Специальность");
            tf1.setRequiredIndicatorVisible(true);
            tf2.setRequiredIndicatorVisible(true);
            tf3.setRequiredIndicatorVisible(true);
            tf4.setRequiredIndicatorVisible(true);
            form.addComponent(tf1);
            form.addComponent(tf2);
            form.addComponent(tf3);
            form.addComponent(tf4);
            subLayout.addComponent(form);
            Button confirmButton = new Button("ОК");
            Button cancelButton = new Button("Отменить");
            subLayout.addComponent(confirmButton);
            subLayout.addComponent(cancelButton);
            addWindow(addDoctorWindow);
            confirmButton.addClickListener((Button.ClickListener) clickEvent1 -> {
                Doctor newDoctor = new Doctor(tf1.getValue(),tf2.getValue(),tf3.getValue(),tf4.getValue());
                try {
                    doctorDAO.setInTable(newDoctor);
                    doctors = doctorDAO.getTable();
                    prescriptionsStat = doctorDAO.getPrescriptionStat(doctors);
                    doctorPrescriptionsStatsGrid.setItems(prescriptionsStat);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                doctorsGrid.setItems(doctors);
                for(Button b: editDoctorButtons){
                    absoluteLayout.removeComponent(b);
                }
                for(Button b: deleteDoctorButtons){
                    absoluteLayout.removeComponent(b);
                }
                createEditDoctorsButtons();
                createDeleteDoctorsButtons();
                addDoctorWindow.close();
            });
            cancelButton.addClickListener((Button.ClickListener) clickEvent12 -> {
                addDoctorWindow.close();
            });
        });
    }
    /**
     *  Функция создания кнопок редактирования докторов
     *  По своей логике аналогична функции создания кнопок редактирования пациентов
     */
    private void createEditDoctorsButtons(){
        for(int i = 0; i< doctors.size();i++){
            Button button = new Button(VaadinIcons.EDIT);
            button.setHeight("39");
            int top = 88 + 38*i;
            editDoctorButtons.add(button);
            int finalI = i;
            button.addClickListener((Button.ClickListener) clickEvent -> {
                Window editDoctorWindow = new Window();
                editDoctorWindow.setWidth("500");
                editDoctorWindow.center();
                VerticalLayout subLayout = new VerticalLayout();
                editDoctorWindow.setContent(subLayout);

                Doctor doctor = doctors.get(finalI);
                Doctor editedDoctor = new Doctor();
                editedDoctor.setId(doctor.getId());

                FormLayout form = new FormLayout();
                TextField tf1 = new TextField("Имя",doctor.getFirstName());
                TextField tf2 = new TextField("Фаимилия",doctor.getLastName());
                TextField tf3 = new TextField("Отчество",doctor.getPatronymic());
                TextField tf4 = new TextField("Специальность",doctor.getSpeciality());
                Button confirmButton = new Button("ОК");
                Button cancelButton = new Button("Отмена");
                confirmButton.setEnabled(false);

                tf1.setRequiredIndicatorVisible(true);
                tf2.setRequiredIndicatorVisible(true);
                tf3.setRequiredIndicatorVisible(true);
                tf4.setRequiredIndicatorVisible(true);
                tf1.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf1.getValue().equals(doctor.getFirstName())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf2.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf2.getValue().equals(doctor.getLastName())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf3.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf3.getValue().equals(doctor.getPatronymic())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf4.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf4.getValue().equals(doctor.getSpeciality())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                confirmButton.addClickListener((Button.ClickListener) clickEvent1 -> {
                    editedDoctor.setFirstName(tf1.getValue());
                    editedDoctor.setLastName(tf2.getValue());
                    editedDoctor.setPatronymic(tf3.getValue());
                    editedDoctor.setSpeciality(tf4.getValue());
                    doctors.set(finalI,editedDoctor);
                    try{
                        doctorDAO.updateTable(editedDoctor);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    doctorsGrid.setItems(doctors);
                    editDoctorWindow.close();
                });
                cancelButton.addClickListener((Button.ClickListener) clickEvent12 -> editDoctorWindow.close());

                form.addComponent(tf1);
                form.addComponent(tf2);
                form.addComponent(tf3);
                form.addComponent(tf4);
                subLayout.addComponent(form);
                subLayout.addComponent(confirmButton);
                subLayout.addComponent(cancelButton);
                addWindow(editDoctorWindow);
            });
            absoluteLayout.addComponent(button,"left:1230px;" + "top:" + top + "px");
        }
    }
    /**
     * Функция создания кнопок удаления докторов
     * По своей логике аналогична функции создания кнопок удаления пациентов
     */
    private void createDeleteDoctorsButtons(){
        for(int i = 0; i < doctors.size();i++){
            Button button = new Button(VaadinIcons.DEL);
            button.setHeight("39");
            deleteDoctorButtons.add(button);
            int top = 88 + 38 * i;
            int finalI = i;
            button.addClickListener((Button.ClickListener) clickEvent -> {
                Doctor deletedDoctor = doctors.get(finalI);
                doctors.remove(finalI);
                prescriptionsStat.remove(finalI);
                doctorsGrid.setItems(doctors);
                doctorPrescriptionsStatsGrid.setItems(prescriptionsStat);
                for(Button b: deleteDoctorButtons){
                    absoluteLayout.removeComponent(b);
                }
                for(Button b:editDoctorButtons){
                    absoluteLayout.removeComponent(b);
                }
                try {
                    doctorDAO.deleteFromTable(deletedDoctor);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                createEditDoctorsButtons();
                createDeleteDoctorsButtons();
            });
            absoluteLayout.addComponent(button,"left:1280px;" + "top:" + top + "px");
        }
    }
    /**
     * Функция инициализации кнопки добавления нового рецепта
     */
    private void initAddPrescriptionButton(){
        addPrescriptionButton.addClickListener((Button.ClickListener) clickEvent -> {
            Window addPrescriptionWindow = new Window();
            addPrescriptionWindow.setWidth("500");
            addPrescriptionWindow.center();
            VerticalLayout subLayout = new VerticalLayout();
            addPrescriptionWindow.setContent(subLayout);
            /*
                Для ввода полей "Пациент", "Врач", "Приоритет" используются ComboBox
                Выбрать врача и пациента можно только из спика уже существующих
                Приоритет выбирается из списка из 3 возможных
                Дата создания инициализируется текущей датой
             */
            FormLayout form = new FormLayout();
            TextField tf1 = new TextField("Описание");
            ComboBox<Patient> selectPatient = new ComboBox<>("Пациент");
            selectPatient.setWidth("300");
            selectPatient.setItems(patients);
            selectPatient.setItemCaptionGenerator(Patient::getPatientData);
            ComboBox<Doctor> selectDoctor = new ComboBox<>("Доктор");
            selectDoctor.setWidth("300");
            selectDoctor.setItems(doctors);
            selectDoctor.setItemCaptionGenerator(Doctor::getDoctorData);
            DateField date = new DateField("Дата создания");
            date.setValue(LocalDate.now());
            TextField tf2 = new TextField("Срок действия");
            ComboBox<String> selectPriority = new ComboBox<>("Приоритет");
            List<String> priority = new ArrayList<>();
            priority.add("Normal");
            priority.add("Cito");
            priority.add("Statim");
            selectPriority.setItems(priority);
            Button confirmButton = new Button("ОК");
            Button cancelButton = new Button("Отмена");
            tf1.setRequiredIndicatorVisible(true);
            selectPatient.setRequiredIndicatorVisible(true);
            selectDoctor.setRequiredIndicatorVisible(true);
            date.setRequiredIndicatorVisible(true);
            tf2.setRequiredIndicatorVisible(true);
            selectPriority.setRequiredIndicatorVisible(true);

            confirmButton.addClickListener((Button.ClickListener) clickEvent1 -> {
                Prescription newPrescription = new Prescription(tf1.getValue(),
                        selectPatient.getValue(),
                        selectDoctor.getValue(),
                        date.getValue(),
                        Integer.parseInt(tf2.getValue()),
                        selectPriority.getValue());

                try {
                    prescriptionDAO.setInTable(newPrescription);
                    prescriptions = prescriptionDAO.getTable();
                    //После добавления нового рецепта обновляется статистика выданных врачами рецептов
                    prescriptionsStat = doctorDAO.getPrescriptionStat(doctors);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                doctorPrescriptionsStatsGrid.setItems(prescriptionsStat);
                prescriptionsGrid.setItems(prescriptions);
                for(Button b: editPrescriptionsButtons){
                    absoluteLayout.removeComponent(b);
                }
                for(Button b: deletePrescriptionsButtons){
                    absoluteLayout.removeComponent(b);
                }
                createEditPrescriptionsButtons();
                createDeletePrescriptionsButtons();

                addPrescriptionWindow.close();
            });
            cancelButton.addClickListener((Button.ClickListener) clickEvent12 -> addPrescriptionWindow.close());

            form.addComponent(tf1);
            form.addComponent(selectPatient);
            form.addComponent(selectDoctor);
            form.addComponent(date);
            form.addComponent(tf2);
            form.addComponent(selectPriority);
            subLayout.addComponent(form);
            subLayout.addComponent(confirmButton);
            subLayout.addComponent(cancelButton);
            addWindow(addPrescriptionWindow);
        });
    }
    /**
     *  Функция создания кнопок редактирования рецептов
     */
    private void createEditPrescriptionsButtons(){
        for(int i =0; i<prescriptions.size();i++){
            Button button = new Button(VaadinIcons.EDIT);
            button.setHeight("39");
            button.setWidth("40");
            int top = 88 + 38*i;
            editPrescriptionsButtons.add(button);
            int finalI = i;
            /*
                Аналогично добавлению рецепта, создается окно с полями для изменения,
                но в данном случае поля инициализированны текущими значениями данных для рецепта
             */
            button.addClickListener((Button.ClickListener) clickEvent -> {
                Window editPrescriptionWindow = new Window();
                editPrescriptionWindow.setWidth("500");
                editPrescriptionWindow.center();
                VerticalLayout subLayout = new VerticalLayout();
                editPrescriptionWindow.setContent(subLayout);

                Prescription prescription = prescriptions.get(finalI);
                Prescription editedPrescription = new Prescription();

                FormLayout form = new FormLayout();
                TextField tf1 = new TextField("Описание",prescription.getDescription());
                ComboBox<Patient> selectPatient = new ComboBox<>("Пациент");
                selectPatient.setWidth("300");
                selectPatient.setItems(patients);
                selectPatient.setValue(prescription.getPatient());
                selectPatient.setItemCaptionGenerator(Patient::getPatientData);
                ComboBox<Doctor> selectDoctor = new ComboBox<>("Доктор");
                selectDoctor.setWidth("300");
                selectDoctor.setItems(doctors);
                selectDoctor.setValue(prescription.getDoctor());
                selectDoctor.setItemCaptionGenerator(Doctor::getDoctorData);
                DateField date = new DateField("Дата создания");
                date.setValue(LocalDate.now());
                TextField tf2 = new TextField("Срок действия", String.valueOf(prescription.getValidityPeriod()));
                ComboBox<String> selectPriority = new ComboBox<>("Приоритет");
                List<String> priority = new ArrayList<>();
                priority.add("Normal");
                priority.add("Cito");
                priority.add("Statim");
                Button confirmButton = new Button("ОК");
                Button cancelButton = new Button("Отмена");
                confirmButton.setEnabled(false);
                selectPriority.setItems(priority);
                selectPriority.setValue(prescription.getPriority());

                tf1.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf1.getValue().equals(prescription.getDescription())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                selectPatient.addValueChangeListener((HasValue.ValueChangeListener<Patient>) valueChangeEvent -> {
                    if(!selectPatient.getValue().equals(prescription.getPatient())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                selectDoctor.addValueChangeListener((HasValue.ValueChangeListener<Doctor>) valueChangeEvent -> {
                    if(!selectDoctor.getValue().equals(prescription.getDoctor())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                date.addValueChangeListener((HasValue.ValueChangeListener<LocalDate>) valueChangeEvent -> {
                    if(!date.getValue().isEqual(prescription.getCreationDate())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                tf2.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!tf2.getValue().equals(String.valueOf(prescription.getValidityPeriod()))){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                selectPriority.addValueChangeListener((HasValue.ValueChangeListener<String>) valueChangeEvent -> {
                    if(!selectPriority.getValue().equals(prescription.getPriority())){
                        confirmButton.setEnabled(true);
                    }
                    else{
                        confirmButton.setEnabled(false);
                    }
                });
                confirmButton.addClickListener((Button.ClickListener) clickEvent1 -> {
                    editedPrescription.setId(prescription.getId());
                    editedPrescription.setDescription(tf1.getValue());
                    editedPrescription.setPatient(selectPatient.getValue());
                    editedPrescription.setDoctor(selectDoctor.getValue());
                    editedPrescription.setCreationDate(date.getValue());
                    editedPrescription.setValidityPeriod(Integer.parseInt(tf2.getValue()));
                    editedPrescription.setPriority(selectPriority.getValue());
                    prescriptions.set(finalI,editedPrescription);
                    try {
                        prescriptionDAO.updateTable(editedPrescription);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    prescriptionsGrid.setItems(prescriptions);
                    editPrescriptionWindow.close();
                });
                cancelButton.addClickListener((Button.ClickListener) clickEvent12 -> editPrescriptionWindow.close());

                form.addComponent(tf1);
                form.addComponent(selectPatient);
                form.addComponent(selectDoctor);
                form.addComponent(date);
                form.addComponent(tf2);
                form.addComponent(selectPriority);
                subLayout.addComponent(form);
                subLayout.addComponent(confirmButton);
                subLayout.addComponent(cancelButton);
                addWindow(editPrescriptionWindow);
            });
            absoluteLayout.addComponent(button,"left:1830px;" + "top:" + top + "px");
        }
    }
    /**
     * Функция создания кнопок удаления рецептов
     */
    private void createDeletePrescriptionsButtons(){
        for(int i = 0; i < prescriptions.size();i++){
            Button button = new Button(VaadinIcons.DEL);
            button.setHeight("39");
            deletePrescriptionsButtons.add(button);
            int top = 88 + 38 * i;
            int finalI = i;
            button.addClickListener((Button.ClickListener) clickEvent -> {
              Prescription deletedPrescription = prescriptions.get(finalI);
              prescriptions.remove(finalI);
              prescriptionsGrid.setItems(prescriptions);
              try {
                  prescriptionDAO.deleteFromTable(deletedPrescription);
              } catch (SQLException throwables) {
                  throwables.printStackTrace();
              }
              for(Button b: editPrescriptionsButtons){
                  absoluteLayout.removeComponent(b);
              }
              for(Button b: deletePrescriptionsButtons){
                  absoluteLayout.removeComponent(b);
              }
              createEditPrescriptionsButtons();
              createDeletePrescriptionsButtons();
            });
            absoluteLayout.addComponent(button,"left:1870px;" + "top:" + top + "px");
        }
    }

    /**
     * Функция, где происходит инициализация базы данных, добавление основных элементов UI и вызов соответсвуюших функций
     * @param vaadinRequest
     */
    @Override
    protected void init(VaadinRequest vaadinRequest) {
        try {
            InitDB initDB = new InitDB();
            patients = patientDAO.getTable();
            doctors = doctorDAO.getTable();
            prescriptionsStat = doctorDAO.getPrescriptionStat(doctors);
            prescriptions = prescriptionDAO.getTable();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        patientsGrid.setHeightMode(HeightMode.ROW);
        patientsGrid.setHeight("900");
        patientsGrid.setWidth("500");
        patientsGrid.setItems(patients);
        patientsGrid.addColumn(Patient::getFirstName).setCaption("Имя");
        patientsGrid.addColumn(Patient::getLastName).setCaption("Фамилия");
        patientsGrid.addColumn(Patient::getPatronymic).setCaption("Отчество");
        patientsGrid.addColumn(Patient::getPhoneNumber).setCaption("Номер телефона");
        initAddPatientButton();
        createEditPatientButtons();
        createDeletePatientButtons();


        doctorsGrid.setHeight("900");
        doctorsGrid.setWidth("500");
        doctorsGrid.setItems(doctors);
        doctorsGrid.addColumn(Doctor::getFirstName).setCaption("Имя");
        doctorsGrid.addColumn(Doctor::getLastName).setCaption("Фамилия");
        doctorsGrid.addColumn(Doctor::getPatronymic).setCaption("Отчество");
        doctorsGrid.addColumn(Doctor::getSpeciality).setCaption("Специальность");
        doctorPrescriptionsStatsGrid.setHeight("900");
        doctorPrescriptionsStatsGrid.setWidth("100");

        doctorPrescriptionsStatsGrid.addColumn(Integer::intValue).setCaption("Статистика");
        doctorPrescriptionsStatsGrid.setItems(prescriptionsStat);
        initAddDoctorButton();
        createEditDoctorsButtons();
        createDeleteDoctorsButtons();

        prescriptionsGrid.setHeight("900");
        prescriptionsGrid.setWidth("500");
        prescriptionsGrid.setItems(prescriptions);
        prescriptionsGrid.addColumn(Prescription::getDescription).setCaption("Описание");
        prescriptionsGrid.addColumn(Prescription::getPatientFIO).setCaption("Пациент");
        prescriptionsGrid.addColumn(Prescription::getDoctorFIO).setCaption("Доктор");
        prescriptionsGrid.addColumn(Prescription::getCreationDate).setCaption("Дата создания");
        prescriptionsGrid.addColumn(Prescription::getValidityPeriod).setCaption("Срок действия");
        prescriptionsGrid.addColumn(Prescription::getPriority).setCaption("Приоритет");
        initAddPrescriptionButton();
        createEditPrescriptionsButtons();
        createDeletePrescriptionsButtons();

        absoluteLayout.addComponent(patientsGrid,"left:0px;top:50px");
        absoluteLayout.addComponent(addPatientButton,"left:0px;top:0px");
        absoluteLayout.addComponent(doctorsGrid,"left:630px;top:50px");
        absoluteLayout.addComponent(doctorPrescriptionsStatsGrid,"left:1130px;top:50px");
        absoluteLayout.addComponent(addDoctorButton,"left:630px;top:0px");
        absoluteLayout.addComponent(prescriptionsGrid,"left:1330px;top:50px");
        absoluteLayout.addComponent(addPrescriptionButton,"left:1330px;top:0px");

        setContent(absoluteLayout);
    }

}