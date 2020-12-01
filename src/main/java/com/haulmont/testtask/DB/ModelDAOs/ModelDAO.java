package com.haulmont.testtask.DB.ModelDAOs;

import com.haulmont.testtask.models.Patient;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс, определяющий основные методы взаимодействия моделей с базой данных
 * @param <T> Тип модели
 */
public interface ModelDAO<T> {
    /**
     * Возвращает все столбцы и строки заданной модели
     * @return List объектов, хранящихся в таблице
     * @throws SQLException
     */
    public List<T> getTable() throws SQLException;

    /**
     * Вставляет в таблицу новую модель
     * @param model Модель, которая будет добавлена в базу данных
     * @throws SQLException
     */
    public void setInTable(T model) throws SQLException;

    /**
     * Обновляет данные для уже существующей модели
     * @param model Новое значение модели
     * @throws SQLException
     */
    public void updateTable(T model) throws SQLException;

    /**
     * Удаляет модель из базы данных
     * @param model Модель подлежащая удалению
     * @throws SQLException
     */
    public void deleteFromTable(T model) throws SQLException;
}
