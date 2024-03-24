package ru.Ildar.AstonRest;


import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.config.AppConfig;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;
import ru.Ildar.AstonRest.config.DataSourceConfigurationTest;

import java.util.Map;

@Testcontainers
@Tag("DockerRequired")
public class PatientDAOTest {
    private static final Map<String, Object> props = DataSourceConfigurationTest.getProps();

    private static final String INIT_SQL = "sql/init.sql";
    private static final int containerPort = 5432;
    private static final int localPort = 5432;


    @Container
    public static PostgreSQLContainer<?> container =
            new PostgreSQLContainer<>( "postgres:15-alpine")
                    .withDatabaseName("Aston")
                    .withUsername("username")
                    .withPassword("password")
                   .withInitScript(INIT_SQL);
    public static PatientDAO patientDao;
    private static JdbcDatabaseDelegate jdbcDatabaseDelegate;

    @BeforeAll
    static void beforeAll() {
        container.start();
        patientDao = AppConfig.getPatientDAOInstance();
        jdbcDatabaseDelegate = new JdbcDatabaseDelegate(container, "");
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

  //  @BeforeEach
  //  void setUp() {
 //       ScriptUtils.runInitScript(jdbcDatabaseDelegate, INIT_SQL);
 //   }

    @Test
    void save() {
    //   PatientDAO patientDAO = new PatientDAO();
    //   PatientDTO patient = new PatientDTO();
    //   patient.setName("Petr");
    //   patient.setAge(12);
    //   patient.setSex("female");
    //   patientDAO.savePatient(patient);
    //   Thread.sleep(10000);
    //   System.out.println(patientDAO.findAllPatients());
    //   System.out.println("Hello");
    }
}
