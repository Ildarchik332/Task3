package ru.Ildar.AstonRest.servlet.patient;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonREST.servlets.patient.ShowPatientServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Mockito.*;

@Tag("test")
@ExtendWith(MockitoExtension.class)
 class ShowPatientServletTest {
    private static PatientDAO mockPatientDao;

    @InjectMocks
    private static ShowPatientServlet showPatientServlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private BufferedReader mockBufferReader;

    private static void setMock(PatientDAO mock) {
        try {
            Field instance = AppConfig.class.getDeclaredField("patientDAOInstance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockPatientDao = mock(PatientDAO.class);
        setMock(mockPatientDao);
        showPatientServlet = new ShowPatientServlet();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockPatientDao);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn(Collections.singletonList(new PatientDTO("Kostya", 33, "male")))
                .when(mockPatientDao).findAllPatients();
        when(mockResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        showPatientServlet.processRequest(mockResponse);

        verify(mockPatientDao).findAllPatients();
        verify(mockResponse).setContentType(ArgumentMatchers.eq("application/json"));
        verify(mockResponse.getWriter())
                .write(ArgumentMatchers.eq("[{\"id\":null,\"name\":\"Kostya\",\"age\":33,\"sex\":\"male\",\"doctorId\":null,\"drugList\":null}]"));
    }
}
