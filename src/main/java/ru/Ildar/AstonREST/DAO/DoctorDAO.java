package ru.Ildar.AstonREST.DAO;

import java.util.Map;
import java.util.Optional;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static ru.Ildar.AstonREST.util.Constant.CommonConstants.PASSWORD;
import static ru.Ildar.AstonREST.util.Constant.CommonConstants.USERNAME;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.DRIVER;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.URL;

/**
 * Сервис для работы с Доктором
 *
 * @author Ildar
 */
public class DoctorDAO {

    private final DataSource dataSource;

    public DoctorDAO(Map<String, Object> dataSourceProperties) {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl((String) dataSourceProperties.get(URL));
        config.setUsername((String) dataSourceProperties.get(USERNAME));
        config.setPassword((String) dataSourceProperties.get(PASSWORD));
        config.setDriverClassName((String) dataSourceProperties.get(DRIVER));

        dataSource = new HikariDataSource(config);
    }

    /**
     * Метод для создания доктора
     *
     * @param doctor данные доктора
     */
    public void saveDoctor(DoctorDTO doctor) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO doctor(name, specialization) VALUES (?, ?)")) {

            ps.setString(1, doctor.getName());
            ps.setString(2, doctor.getSpecialization());

            ps.executeUpdate();


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод для поиска всех докторов
     *
     * @return лист со всеми данными
     */
    public List<DoctorDTO> findAllDoctors() {
        List<DoctorDTO> doctors = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT id, name, specialization FROM doctor")) {

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String specialization = resultSet.getString("specialization");

                doctors.add(new DoctorDTO(name, specialization));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return doctors;
    }

    /**
     * Метод для поиска пациентов конкретного доктора
     *
     * @param id идентификатор доктора
     * @return доктор и лист с его пациентами
     */
    public DoctorDTO findDoctorAndPatients(Long id) {
        List<PatientDTO> patients = new ArrayList<>();
        DoctorDTO doctor = new DoctorDTO();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT d.id, d.name as doctor_name," +
                             " d.specialization as doctor_specialization, p.name as patient_name, p.age, p.sex "
                             +
                             "FROM Doctor d LEFT JOIN Patient p ON d.id = p.doctor_id WHERE d.id = ?")) {

            ps.setLong(1, id);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                String doctorName = resultSet.getString("doctor_name");
                String doctorSpecialization = resultSet.getString("doctor_specialization");
                String name = resultSet.getString("patient_name");
                Integer age = resultSet.getInt("age");
                String sex = resultSet.getString("sex");

                if (doctor.getName() == null) {
                    doctor.setName(doctorName);
                }
                if (doctor.getSpecialization() == null) {
                    doctor.setSpecialization(doctorSpecialization);
                }
                patients.add(new PatientDTO(name, age, sex));
            }
            doctor.setPatientList(patients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctor;

    }

    /**
     * Метод для поиска доктора по идентификатору
     *
     * @param id идентификатор доктора
     * @return данные доктора
     */
    public Optional<DoctorDTO> findDoctorById(Long id) {
        DoctorDTO doctor = null;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "SELECT * FROM doctor where id = ?")) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                doctor = new DoctorDTO();

                doctor.setName(resultSet.getString("name"));
                doctor.setSpecialization(resultSet.getString("specialization"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(doctor);
    }

    /**
     * Метод дл обновления данных доктора
     *
     * @param id           идентификатор доктора
     * @param updateDoctor обновленные данные
     */
    public void updateDoctor(Long id, DoctorDTO updateDoctor) {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            ;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        StringBuilder query = new StringBuilder("UPDATE doctor SET ");
        List<Object> params = new ArrayList<>();

        String name = updateDoctor.getName();
        if (name != null) {
            query.append("name = ?, ");
            params.add(name);
        }

        String specialization = updateDoctor.getSpecialization();
        if (specialization != null) {
            query.append("specialization = ?, ");
            params.add(specialization);
        }

        // Удаление последней запятой и пробела
        query.delete(query.length() - 2, query.length());

        if (id != null) {
            query.append(" WHERE id = ?");
            params.add(id);
        } else {
            throw new IllegalArgumentException("Не передан идентификатор доктора для обновления !");
        }

        AppConfig.updateEntity(query, params, connection);

    }

    /**
     * Метод для удаления доктора по его идентификатору
     *
     * @param id идентификатор доктора
     */
    public boolean deleteDoctorById(Long id) {
        boolean deleteResult = false;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM doctor WHERE id = ?")) {
            ps.setLong(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return deleteResult;
    }
}

