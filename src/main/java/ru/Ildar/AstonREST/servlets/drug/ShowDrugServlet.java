package ru.Ildar.AstonREST.servlets.drug;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Сервлет для просмотра всех лекарств
 *
 * @author Ildar
 */
@WebServlet(name = "ShowDrugServlet", urlPatterns = "/drugs")
public class ShowDrugServlet extends HttpServlet {

    private final DrugDAO drugDAO = AppConfig.getDrugDAOInstance();
    private final ObjectMapper objectMapper = AppConfig.getObjectMapperInstance();

    private static final String DRUG_SHOW_ERROR = "Произошла ошибка, лекарства не найдены !";


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

        try {
            List<DrugDTO> allDrugs = drugDAO.findAllDrugs();
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(allDrugs));
        } catch (IOException e) {
            resp.setContentType("text/plain");
            resp.getWriter().write(DRUG_SHOW_ERROR);
        }
    }

}
