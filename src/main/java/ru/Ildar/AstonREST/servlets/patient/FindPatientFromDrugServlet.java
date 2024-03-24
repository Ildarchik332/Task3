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
 * Сервлет для поиска пациентов по лекарству
 *
 * @author Ildar
 */
@WebServlet(name = "FindPatientFromDrugServlet", urlPatterns = "/findPatientDr")
public class FindPatientFromDrugServlet extends HttpServlet {

    private final PatientDAO patientDAO = AppConfig.getPatientDAOInstance();

    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DRUG_SHOW_ERROR = "Произошла ошибка, пациенты с такими набором лекарств не найдены !";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String drugId = req.getParameter("drugId");
            List<PatientDTO> patientAndDrug = patientDAO.findPatientDrugs(Long.valueOf(drugId));
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(patientAndDrug));
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DRUG_SHOW_ERROR);
        }
    }
}
