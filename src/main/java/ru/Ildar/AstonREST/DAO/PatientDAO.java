package ru.Ildar.AstonREST.DAO;


import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.db.DataSourceConfiguration;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с пациентом
 *
 * @author Ildar
 */
public class PatientDAO {

    /**
     * Метод для создания пациента
     *
     * @param patient Данные пациента
     */
    public void savePatient(PatientDTO patient) {
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO patient(name, age, sex) VALUES (?, ?, ?)")) {

            ps.setString(1, patient.getName());
            ps.setInt(2, patient.getAge());
            ps.setString(3, patient.getSex());

            ps.executeUpdate();


        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод для поиска всех пациентов
     *
     * @return лист со всеми данными
     */
    public List<PatientDTO> findAllPatients() {
        List<PatientDTO> patients = new ArrayList<>();

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM patient")) {

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                Integer age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");
                Long doctorId = resultSet.getLong("doctor_id");

                patients.add(new PatientDTO(name, age, sex, doctorId));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return patients;
    }

    public List<PatientDTO> findPatientDrugs(Long drugId) {
        List<PatientDTO> patients = new ArrayList<>();
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT p.id, p.name, p.age FROM patient p " +
                     "JOIN patient_drug pr ON p.id = pr.patient_id " +
                     "WHERE pr.drug_id = ?")) {

            ps.setLong(1, drugId);

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                PatientDTO patient = new PatientDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("name"),
                        resultSet.getInt("age"));
                patients.add(patient);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patients;
    }

    /**
     * Метод для поиска пациента по идентификатору
     *
     * @param id идентификатор пациента
     * @return данные пациента
     */
    public PatientDTO findByPatientId(Long id) {
        PatientDTO patient = null;

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM patient where id = ?")) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                patient = new PatientDTO();

                patient.setName(resultSet.getString("name"));
                patient.setAge(resultSet.getInt("age"));
                patient.setSex(resultSet.getString("sex"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return patient;
    }

    /**
     * Метод для обновления данных пациента
     *
     * @param id            идентификатор пациента
     * @param updatePatient обновленные данные
     */
    public void updatePatient(Integer id, PatientDTO updatePatient) {
        Connection connection = null;
        try {
            connection = DataSourceConfiguration.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringBuilder query = new StringBuilder("UPDATE patient SET ");
        List<Object> params = new ArrayList<>();

        String name = updatePatient.getName();
        if (name != null) {
            query.append("name = ?, ");
            params.add(name);
        }

        Integer age = updatePatient.getAge();
        if (age != null) {
            query.append("age = ?, ");
            params.add(age);
        }

        String sex = updatePatient.getSex();
        if (sex != null) {
            query.append("sex = ?, ");
            params.add(sex);
        }

        Long doctorId = updatePatient.getDoctorId();
        if (doctorId != null) {
            query.append("doctor_id = ?, ");
            params.add(doctorId);
        }

        // Удаление последней запятой и пробела
        query.delete(query.length() - 2, query.length());

        if (id != null) {
            query.append(" WHERE id = ?");
            params.add(id);
        } else {
            throw new IllegalArgumentException("Не передан идентификатор пациента для обновления !");
        }

        AppConfig.getConnection(query, params, connection);

    }

    /**
     * Метод для удаления пациента по идентификатору
     *
     * @param id идентификатор пациента
     */
    public void deletePatientById(Integer id) {
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM patient WHERE id = ?")) {
            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
