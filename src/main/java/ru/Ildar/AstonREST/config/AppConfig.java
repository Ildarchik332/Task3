package ru.Ildar.AstonREST.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.db.DataSourceConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
//Перенести в утил
public class AppConfig {

    private static DoctorDAO doctorDAOInstance = null;
    private static PatientDAO patientDAOInstance = null;
    private static DrugDAO drugDAOInstance = null;
    private static ObjectMapper objectMapperInstance = null;


    public static DoctorDAO getDoctorDAOInstance() {
        if (doctorDAOInstance == null) {
            doctorDAOInstance = new DoctorDAO(DataSourceConfiguration.getDataSourceProperties());
        }
        return doctorDAOInstance;
    }

    public static PatientDAO getPatientDAOInstance() {
        if (patientDAOInstance == null) {
            patientDAOInstance = new PatientDAO(DataSourceConfiguration.getDataSourceProperties());
        }
        return patientDAOInstance;
    }

    public static DrugDAO getDrugDAOInstance(){
        if(drugDAOInstance == null){
            drugDAOInstance = new DrugDAO(DataSourceConfiguration.getDataSourceProperties());
        }
        return drugDAOInstance;
    }

    public static ObjectMapper getObjectMapperInstance() {
        if (objectMapperInstance == null) {
            objectMapperInstance = new ObjectMapper();
        }
        return objectMapperInstance;
    }

    public static void updateEntity(StringBuilder query, List<Object> params, Connection connection) {
        try (PreparedStatement ps = connection.prepareStatement(query.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            ps.executeUpdate();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
    }

}
