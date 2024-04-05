package ru.Ildar.AstonRest.servlet.drug;

import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.servlets.drug.DeleteDrugServlet;
import ru.Ildar.AstonREST.servlets.patient.DeletePatientServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class DeleteDrugServletTest {
    private static DrugDAO mockDrugDao;

    @InjectMocks
    private static DeleteDrugServlet deleteDrugServlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private BufferedReader mockBufferReader;

    private static void setMock(DrugDAO mock) {
        try {
            Field instance = AppConfig.class.getDeclaredField("drugDAOInstance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockDrugDao = Mockito.mock(DrugDAO.class);
        setMock(mockDrugDao);
        deleteDrugServlet = new DeleteDrugServlet();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockDrugDao);
    }

    @Test
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("2").when(mockRequest).getParameter("id");

        deleteDrugServlet.processRequest(mockRequest, mockResponse);

        Mockito.verify(mockDrugDao).deleteDrugById(Mockito.anyLong());
    }
}
