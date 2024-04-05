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
import java.util.List;

/**
 * Сервлет для просмотра всех докторов
 *
 * @author Ildar
 */
@WebServlet(name = "ShowDoctorServlet", urlPatterns = "/doctors")
public class ShowDoctorServlet extends HttpServlet {

    private final DoctorDAO doctorDAO = AppConfig.getDoctorDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DOCTOR_SHOW_ERROR = "Произошла ошибка, доктора не найдены !";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        processRequest(resp);
    }

    public void processRequest(HttpServletResponse resp) throws IOException {
        try {
            List<DoctorDTO> allDoctors = doctorDAO.findAllDoctors();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(allDoctors));
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DOCTOR_SHOW_ERROR);
        }
    }
}
