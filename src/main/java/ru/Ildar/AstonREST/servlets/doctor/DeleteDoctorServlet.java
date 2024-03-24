package ru.Ildar.AstonREST.servlets.doctor;

import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.config.AppConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для удаления доктора
 *
 * @author Ildar
 */
@WebServlet(name = "DeleteDoctorServlet", urlPatterns = "/deleteDoctor")
public class DeleteDoctorServlet extends HttpServlet {

    private final DoctorDAO doctorDao = AppConfig.getDoctorDAOInstance();

    private static final String DOCTOR_DELETE_ERROR = "Произошла ошибка при удаление доктора !";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String id = req.getParameter("id");
            doctorDao.deleteDoctorById(Integer.valueOf(id));
            resp.setContentType("application/json");
            resp.getWriter().write("Doctor is deleted!");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DOCTOR_DELETE_ERROR);
        }
    }
}
