package ru.Ildar.AstonREST.servlets.patient;

import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для удаления пациента
 *
 * @author Ildar
 */
@WebServlet(name = "DeletePatientServlet", urlPatterns = "/deletePatient")
public class DeletePatientServlet extends HttpServlet {

    private final PatientDAO patientDAO = AppConfig.getPatientDAOInstance();

    private static final String PATIENT_DELETE_ERROR = "Произошла ошибка при удаление пациента !";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String id = req.getParameter("id");
            patientDAO.deletePatientById(Integer.valueOf(id));
            resp.setContentType("application/json");
            resp.getWriter().write("Patient is deleted!");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(PATIENT_DELETE_ERROR);
        }

    }
}
