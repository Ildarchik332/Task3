package ru.Ildar.AstonREST.servlets.patient;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет для просмотра всех пацентов
 *
 * @author Ildar
 */
@WebServlet(name = "ShowPatientServlet", urlPatterns = "/patients")
public class ShowPatientServlet extends HttpServlet {


    private final PatientDAO patientDAO = AppConfig.getPatientDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String PATIENT_SHOW_ERROR = "Произошла ошибка, пациенты не найдены !";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<PatientDTO> allPatients = patientDAO.findAllPatients();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(allPatients));
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(PATIENT_SHOW_ERROR);
        }
    }

}