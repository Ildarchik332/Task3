package ru.Ildar.AstonREST.servlets.patient;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для добавления пациента
 *
 * @author Ildar
 */
@WebServlet(name = "AddPatientServlet", urlPatterns = "/addPatient")
public class AddPatientServlet extends HttpServlet {

    private final PatientDAO patientDAO = AppConfig.getPatientDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String PATIENT_ADD_ERROR = "Произошла ошибка, пациент не добавлен !";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            PatientDTO dto = objectMapper.readValue(req.getReader(), PatientDTO.class);
            patientDAO.savePatient(dto);
            resp.setContentType("application/json");
            resp.getWriter().write("Patient is created!");

        } catch (JsonParseException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write("Ошибка при парсинге данных пациента");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(PATIENT_ADD_ERROR);
        }
    }
}
