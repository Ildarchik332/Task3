package ru.Ildar.AstonRest.servlet.drug;

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
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonREST.servlets.drug.AddDrugServlet;
import ru.Ildar.AstonREST.servlets.drug.DeleteDrugServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class AddDrugServletTest {
    private static DrugDAO mockDrugDao;

    @InjectMocks
    private static AddDrugServlet addDrugServlet;

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
        addDrugServlet = new AddDrugServlet();
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
    void doPost() throws IOException {
        String expectedTitle = "New title";
        Mockito.doReturn(mockBufferReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedTitle + "\"}",
                null
        ).when(mockBufferReader).readLine();

        addDrugServlet.processRequest(mockRequest, mockResponse);

        ArgumentCaptor<DrugDTO> argumentCaptor = ArgumentCaptor.forClass(DrugDTO.class);
        Mockito.verify(mockDrugDao).saveDrug(argumentCaptor.capture());

        DrugDTO result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedTitle, result.getTitle());
    }
}
