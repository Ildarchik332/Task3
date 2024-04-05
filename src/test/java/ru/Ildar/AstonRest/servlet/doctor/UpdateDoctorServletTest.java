package ru.Ildar.AstonRest.servlet.doctor;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.servlets.doctor.AddDoctorServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.reflect.Field;

@Tag("test")
@ExtendWith(MockitoExtension.class)
class UpdateDoctorServletTest {
    private static DoctorDAO mockDoctorDAO;

    @InjectMocks
    private static AddDoctorServlet addDoctorServlet;

    @Mock
    private HttpServletRequest mockRequest;

    @Mock
    private HttpServletResponse mockResponse;

    @Mock
    private BufferedReader mockBufferReader;

    private static void setMock(DoctorDAO mock) {
        try {
            Field instance = DoctorDAO.class.getDeclaredField("instance");
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
        addDoctorServlet = new AddDoctorServlet();
    }

    @BeforeEach
    void setUp() throws IOException {
        Mockito.doReturn(new PrintWriter(Writer.nullWriter())).when(mockResponse).getWriter();
    }

    @AfterEach
    void tearDown() {
        Mockito.reset(mockDoctorDAO);
    }

}
