package ru.Ildar.AstonREST.servlets.drug;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Сервлет для добавления лекарства
 *
 * @author Ildar
 */
@WebServlet(name = "AddDrugServlet", urlPatterns = "/addDrug")
public class AddDrugServlet extends HttpServlet {

    private final DrugDAO drugDAO = AppConfig.getDrugDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DRUG_ADD_ERROR = "Произошла ошибка, лекарство не добавлено !";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        processRequest(req, resp);

    }

    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            DrugDTO dto = objectMapper.readValue(req.getReader(), DrugDTO.class);
            drugDAO.saveDrug(dto);
            resp.setContentType("application/json");
            resp.getWriter().write("Drug is created!");

        } catch (JsonParseException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write("Ошибка при парсинге данных лекарства");
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DRUG_ADD_ERROR);
        }
    }

}
