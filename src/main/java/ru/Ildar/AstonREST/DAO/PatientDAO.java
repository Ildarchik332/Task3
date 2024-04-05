package ru.Ildar.AstonREST.DAO;


import java.util.Map;
import java.util.Optional;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static ru.Ildar.AstonREST.util.Constant.CommonConstants.PASSWORD;
import static ru.Ildar.AstonREST.util.Constant.CommonConstants.USERNAME;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.DRIVER;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.URL;

/**
 * Сервис для работы с пациентом
 *
 * @author Ildar
 */
public class PatientDAO {

     private final DataSource dataSource;

     public PatientDAO(Map<String, Object> dataSourceProperties) {
         HikariConfig config = new HikariConfig();
         config.setJdbcUrl((String) dataSourceProperties.get(URL));
         config.setUsername((String) dataSourceProperties.get(USERNAME));
         config.setPassword((String) dataSourceProperties.get(PASSWORD));
         config.setDriverClassName((String) dataSourceProperties.get(DRIVER));

         dataSource = new HikariDataSource(config);
     }

    /**
     * Метод для создания пациента
     *
     * @param patient Данные пациента
     */
    public void savePatient(PatientDTO patient) {
        try (Connection connection = dataSource.getConnection();
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

        try (Connection connection =dataSource.getConnection();
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
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                "SELECT p.id, p.name, p.age FROM patient p " +
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
    public Optional<PatientDTO> findPatientById(Long id) {
        PatientDTO patient = null;

        try (Connection connection = dataSource.getConnection();
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
        return Optional.ofNullable(patient);
    }

    /**
     * Метод для обновления данных пациента
     *
     * @param id            идентификатор пациента
     * @param updatePatient обновленные данные
     */
    public void updatePatient(Long id, PatientDTO updatePatient) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
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
            throw new IllegalArgumentException(
                "Не передан идентификатор пациента для обновления !");
        }

        AppConfig.updateEntity(query, params, connection);

    }

    /**
     * Метод для удаления пациента по идентификатору
     *
     * @param id идентификатор пациента
     */
    public boolean deletePatientById(Long id) {
        boolean deleteResult = false;
        try (Connection connection = dataSource.getConnection();
            PreparedStatement ps = connection.prepareStatement(
                "DELETE FROM patient WHERE id = ?")) {
            ps.setLong(1, id);

            deleteResult = ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleteResult;
    }
}
