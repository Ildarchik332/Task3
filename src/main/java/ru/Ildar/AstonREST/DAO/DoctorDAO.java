package ru.Ildar.AstonREST.DAO;

import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.db.DataSourceConfiguration;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с Доктором
 *
 * @author Ildar
 */
public class DoctorDAO {

    /**
     * Метод для создания доктора
     *
     * @param doctor данные доктора
     */
    public void saveDoctor(DoctorDTO doctor) {
        try (Connection connection = DataSourceConfiguration.getConnection();
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

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id, name, specialization FROM doctor")) {

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
    public DoctorDTO findDoctorAndPatients(Integer id) {
        List<PatientDTO> patients = new ArrayList<>();
        DoctorDTO doctor = new DoctorDTO();

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT d.id, d.name as doctor_name," +
                     " d.specialization as doctor_specialization, p.name as patient_name, p.age, p.sex " +
                     "FROM Doctor d LEFT JOIN Patient p ON d.id = p.doctor_id WHERE d.id = ?")) {

            ps.setInt(1, id);

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
    public DoctorDTO findDoctorById(Integer id) {
        DoctorDTO doctor = null;

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM doctor where id = ?")) {

            ps.setInt(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                doctor = new DoctorDTO();

                doctor.setName(resultSet.getString("name"));
                doctor.setSpecialization(resultSet.getString("specialization"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return doctor;
    }

    /**
     * Метод дл обновления данных доктора
     *
     * @param id           идентификатор доктора
     * @param updateDoctor обновленные данные
     */
    public void updateDoctor(Integer id, DoctorDTO updateDoctor) {
        Connection connection = null;
        try {
            connection = DataSourceConfiguration.getConnection();
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

        AppConfig.getConnection(query, params, connection);

    }

    /**
     * Метод для удаления доктора по его идентификатору
     *
     * @param id идентификатор доктора
     */
    public void deleteDoctorById(Integer id) {
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM doctor WHERE id = ?")) {
            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

