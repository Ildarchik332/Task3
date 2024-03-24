package ru.Ildar.AstonREST.servlets.drug;

import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для удаления лекарства
 *
 * @author Ildar
 */
@WebServlet(name = "DeleteDrugServlet", urlPatterns = "/deleteDrug")
public class DeleteDrugServlet extends HttpServlet {

    private final DrugDAO drugDAO = AppConfig.getDrugDAOInstance();

    private static final String DRUG_DELETE_ERROR = "Произошла ошибка при удаление доктора !";


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            String id = req.getParameter("id");
            drugDAO.deleteDrugById(Long.valueOf(id));
            resp.setContentType("application/json");
            resp.getWriter().write("Drug is deleted!");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DRUG_DELETE_ERROR);
        }
    }
}
