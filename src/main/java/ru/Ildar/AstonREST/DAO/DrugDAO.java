package ru.Ildar.AstonREST.DAO;


import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.db.DataSourceConfiguration;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Сервис для работы с лекарством
 *
 * @author Ildar
 */
public class DrugDAO {

    /**
     * Метод для создания лекарства
     *
     * @param drug данные лекарства
     */
    public void saveDrug(DrugDTO drug) {
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement(
                     "INSERT INTO drug (title) VALUES (?)")) {

            ps.setString(1, drug.getTitle());

            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Метод для поиска всех лекарств
     *
     * @return лист со всеми данными
     */
    public List<DrugDTO> findAllDrugs() {
        List<DrugDTO> drugs = new ArrayList<>();

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT id, title FROM drug")) {

            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                String title = resultSet.getString("title");


                drugs.add(new DrugDTO(title));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return drugs;
    }

    /**
     * Метод для получения лекарств пациента по его идентификатору
     *
     * @param patientId идентификатор доктора
     * @return лекарства пациента
     */
    public List<DrugDTO> findDrugPatients(Long patientId) {

        List<DrugDTO> drugs = new ArrayList<>();
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT d.id, d.title FROM drug d " +
                     "JOIN patient_drug pr ON d.id = pr.drug_id " +
                     "WHERE pr.patient_id = ?")) {

            ps.setLong(1, patientId);

            ResultSet resultSet = ps.executeQuery();
            while (resultSet.next()) {
                DrugDTO drug = new DrugDTO(
                        resultSet.getLong("id"),
                        resultSet.getString("title"));
                drugs.add(drug);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drugs;

    }

    /**
     * Метод для поиска лекарства по идентификатору
     *
     * @param id идентификатор лекарства
     * @return данные лекарства
     */
    public DrugDTO findDrugById(Long id) {
        DrugDTO drug = null;

        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("SELECT * FROM drug where id = ?")) {

            ps.setLong(1, id);
            ResultSet resultSet = ps.executeQuery();

            while (resultSet.next()) {
                drug = new DrugDTO();

                drug.setTitle(resultSet.getString("drug"));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }
        return drug;
    }

    /**
     * Метод для обновления данных лекарства
     *
     * @param id         идентификатор лекарства
     * @param updateDrug обновленные данные
     */
    public void updateDrug(Long id, DrugDTO updateDrug) {
        Connection connection = null;
        try {
            connection = DataSourceConfiguration.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        StringBuilder query = new StringBuilder("UPDATE drug SET ");
        List<Object> params = new ArrayList<>();

        String title = updateDrug.getTitle();
        if (title != null) {
            query.append("title = ?, ");
            params.add(title);
        }

        // Удаление последней запятой и пробела
        query.delete(query.length() - 2, query.length());

        if (id != null) {
            query.append(" WHERE id = ?");
            params.add(id);
        } else {
            throw new IllegalArgumentException("Не передан идентификатор лекарства для обновления !");
        }

        AppConfig.getConnection(query, params, connection);
    }

    /**
     * Метод для удаления лекарства по идентификатору
     *
     * @param id идентификатор лекарства
     */
    public void deleteDrugById(Long id) {
        try (Connection connection = DataSourceConfiguration.getConnection();
             PreparedStatement ps = connection.prepareStatement("DELETE FROM drug WHERE id = ?")) {
            ps.setLong(1, id);

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
