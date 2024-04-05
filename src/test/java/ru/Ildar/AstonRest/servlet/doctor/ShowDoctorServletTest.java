package ru.Ildar.AstonRest.servlet.doctor;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonREST.servlets.doctor.ShowDoctorServlet;

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
class ShowDoctorServletTest {
    private static DoctorDAO mockDoctorDAO;

    @InjectMocks
    private static ShowDoctorServlet showDoctorServlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private BufferedReader mockBufferReader;

    private static void setMock(DoctorDAO mock) {
        try {
            Field instance = AppConfig.class.getDeclaredField("doctorDAOInstance");
            instance.setAccessible(true);
            instance.set(instance, mock);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void beforeAll() {
        mockDoctorDAO = Mockito.mock(DoctorDAO.class);
        setMock(mockDoctorDAO);
        showDoctorServlet = new ShowDoctorServlet();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockDoctorDAO);
    }

    @Test
    void doGetAll() throws IOException {
        Mockito.doReturn(Collections.singletonList(new DoctorDTO("Konstantin", "surgeon")))
                .when(mockDoctorDAO).findAllDoctors();
        when(mockResponse.getWriter()).thenReturn(mock(PrintWriter.class));

        showDoctorServlet.processRequest(mockResponse);

        verify(mockDoctorDAO).findAllDoctors();
        verify(mockResponse).setContentType(ArgumentMatchers.eq("application/json"));
        verify(mockResponse.getWriter())
                .write(ArgumentMatchers.eq("[{\"id\":null,\"name\":\"Konstantin\",\"specialization\":\"surgeon\"," +
                        "\" patientList\":null}]"));

    }
}
