package ru.Ildar.AstonRest.servlet.doctor;

import com.github.dockerjava.api.exception.NotFoundException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.servlets.doctor.DeleteDoctorServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class DeleteDoctorServletTest {
    private static DoctorDAO mockDoctorDAO;

    @InjectMocks
    private static DeleteDoctorServlet deleteDoctorServlet;

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
        deleteDoctorServlet = new DeleteDoctorServlet();
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
    void doDelete() throws IOException, NotFoundException {
        Mockito.doReturn("2").when(mockRequest).getParameter("id");

        deleteDoctorServlet.processRequest(mockRequest, mockResponse);

        Mockito.verify(mockDoctorDAO).deleteDoctorById(Mockito.anyLong());
    }
}
