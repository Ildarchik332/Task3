package ru.Ildar.AstonRest.servlet.drug;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonREST.servlets.drug.ShowDrugServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class ShowDrugServletTest {
    private static DrugDAO mockDrugDao;

    @InjectMocks
    private static ShowDrugServlet showDrugServlet;

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
        showDrugServlet = new ShowDrugServlet();
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
    void doGetAll() throws IOException {
        Mockito.doReturn(Collections.singletonList(new DrugDTO("ointment")))
                .when(mockDrugDao).findAllDrugs();
        when(mockResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        showDrugServlet.processRequest(mockResponse);

        verify(mockDrugDao).findAllDrugs();
        verify(mockResponse).setContentType(ArgumentMatchers.eq("application/json"));
        verify(mockResponse.getWriter())
                .write(ArgumentMatchers.eq("[{\"id\":null,\"title\":\"ointment\",\"whoAccepts\":null,\"patientId\":null}]"));

    }
}