package ru.Ildar.AstonREST.servlets.doctor;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для поиска пациентов конкретного доктора
 *
 * @author Ildar
 */
@WebServlet(name = "FindPatientsFromDoctorServlet", urlPatterns = "/findPatients")
public class FindPatientsFromDoctorServlet extends HttpServlet {

    private final DoctorDAO doctorDAO = AppConfig.getDoctorDAOInstance();

    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DOCTOR_SHOW_ERROR = "Произошла ошибка, доктора не найдены !";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String doctorId = req.getParameter("doctorId");
            DoctorDTO doctorAndPatients = doctorDAO.findDoctorAndPatients(Long.valueOf(doctorId));
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(doctorAndPatients));
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DOCTOR_SHOW_ERROR);
        }
    }
}
