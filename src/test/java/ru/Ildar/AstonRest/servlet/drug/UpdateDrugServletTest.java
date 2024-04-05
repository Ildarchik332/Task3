package ru.Ildar.AstonRest.servlet.drug;

import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;
import ru.Ildar.AstonREST.servlets.drug.DeleteDrugServlet;
import ru.Ildar.AstonREST.servlets.drug.UpdateDrugServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
@Tag("test")
@ExtendWith(MockitoExtension.class)
class UpdateDrugServletTest {
    private static DrugDAO mockDrugDao;

    @InjectMocks
    private static UpdateDrugServlet updateDrugServlet;

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
        updateDrugServlet = new UpdateDrugServlet();
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
    void doPut() throws IOException, NotFoundException {
        String expectedTitle = "Update drug";

        Mockito.doReturn("2").when(mockRequest).getParameter("id");
        Mockito.doReturn(mockBufferReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"id\": 4,\"name\": \"" +
                        expectedTitle + "\"}",
                null
        ).when(mockBufferReader).readLine();

        updateDrugServlet.processRequest(mockRequest, mockResponse);

        ArgumentCaptor<DrugDTO> argumentCaptor = ArgumentCaptor.forClass(DrugDTO.class);
    //    Mockito.verify(mockDrugDao).updateDrug(id, argumentCaptor.capture());

        DrugDTO result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedTitle, result.getTitle());
    }

}
