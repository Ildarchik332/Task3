package ru.Ildar.AstonREST.servlets.doctor;

import com.fasterxml.jackson.core.JsonParseException;
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
 * Сервлет для обновления данных доктора
 *
 * @author Ildar
 */
@WebServlet(name = "UpdateDoctorServlet", urlPatterns = "/updateDoctor")
public class UpdateDoctorServlet extends HttpServlet {

    private final DoctorDAO doctorDAO = AppConfig.getDoctorDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DOCTOR_UPDATE_ERROR = "Произошла ошибка при обновлении данных доктора !";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        processRequest(req, resp);
    }

    private void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            DoctorDTO dto = objectMapper.readValue(req.getReader(), DoctorDTO.class);
            doctorDAO.updateDoctor(Long.valueOf(req.getParameter("id")), dto);
            resp.setContentType("application/json");
            resp.getWriter().write("Doctor is updated!");
        } catch (JsonParseException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write("Ошибка при парсинге данных доктора");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DOCTOR_UPDATE_ERROR);
        }
    }
}
