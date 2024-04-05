package ru.Ildar.AstonRest.servlet.patient;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonREST.servlets.patient.AddPatientServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class AddPatientServletTest {
    private static PatientDAO mockPatientDao;

    private static AddPatientServlet addPatientServlet;

    private final static PatientDTO patientDTO = PatientDTO.builder()
            .name("Ilya")
            .build();
    @Mock
    private ObjectMapper objectMapper;
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
        mockPatientDao = Mockito.mock(PatientDAO.class);
        setMock(mockPatientDao);
        addPatientServlet = new AddPatientServlet();
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
    void doPost() throws IOException {
        String expectedName = "Ilya";

    ;
        Mockito.doReturn(mockBufferReader).when(mockRequest).getReader();
        Mockito.doReturn(
                "{\"name\":\"" + expectedName + "\"}",
                null
        ).when(mockBufferReader).readLine();

        when(objectMapper.readValue(mockBufferReader, PatientDTO.class)).thenReturn(patientDTO);
        when(mockResponse.getWriter()).thenReturn(mock(PrintWriter.class));


        addPatientServlet.processRequest(mockRequest, mockResponse);
        ArgumentCaptor<PatientDTO> argumentCaptor = ArgumentCaptor.forClass(PatientDTO.class);
        Mockito.verify(mockPatientDao).savePatient(argumentCaptor.capture());

        PatientDTO result = argumentCaptor.getValue();
        Assertions.assertEquals(expectedName, result.getName());
        //   verify(mockPatientDao).savePatient(patientDTO);
        verify(mockResponse).setContentType(ArgumentMatchers.eq("text/plain"));
        verify(mockResponse.getWriter()).write(ArgumentMatchers.eq("Patient is created"));
    }
}
