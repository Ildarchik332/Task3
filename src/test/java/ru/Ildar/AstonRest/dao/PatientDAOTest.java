package ru.Ildar.AstonRest.dao;


import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import ru.Ildar.AstonREST.DAO.PatientDAO;
import ru.Ildar.AstonREST.models.DTO.PatientDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static ru.Ildar.AstonREST.util.Constant.CommonConstants.PASSWORD;
import static ru.Ildar.AstonREST.util.Constant.CommonConstants.USERNAME;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.DRIVER;
import static ru.Ildar.AstonREST.util.Constant.JDBCFieldsConstants.URL;

@Testcontainers
@Tag("DockerRequired")
class PatientDAOTest {

    private static final String INIT_SQL = "sql/init.sql";
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:15-alpine")
            .withInitScript(INIT_SQL);

    private final Map<String, Object> dataSourceProperties = new HashMap<String, Object>() {{
        put(URL, container.getJdbcUrl());
        put(USERNAME, container.getUsername());
        put(PASSWORD, container.getPassword());
        put(DRIVER, container.getDriverClassName());
    }};

    public final PatientDAO patientDao = new PatientDAO(dataSourceProperties);


    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void findAllPatients() {
        List<PatientDTO> patients = patientDao.findAllPatients();
        Assertions.assertEquals(3, patients.size() );
        PatientDTO actual = patients.get(0);
        Assertions.assertEquals("Sergei", actual.getName());
    }

    @Test
    void savePatient() {
        String expectedName = "New name";
        Integer expectedAge = 15;
        String expectedSex = "male";

        PatientDTO patient = new PatientDTO(
            expectedName,
            expectedAge,
            expectedSex);
        patientDao.savePatient(patient);

        Optional<PatientDTO> resultPatient = patientDao.findPatientById(1L);

       Assertions.assertTrue(resultPatient.isPresent());
    }

    @Test
    void updatePatient() {
        String expectedName = "Update Name";

        PatientDTO patient = patientDao.findAllPatients().get(1);
        patient.setId(1L);
        patient.setName(expectedName);

        patientDao.updatePatient(patient.getId(), patient);
        Assertions.assertEquals(expectedName, patient.getName());

    }

    @Test
    void deletePatientById() {
        Boolean expectedValue = true;
        int expectedSize = patientDao.findAllPatients().size();

        PatientDTO patient = new PatientDTO(
            "User for delete name",
            12,
            "male"
        );
        patientDao.savePatient(patient);

        boolean deleteResult = patientDao.deletePatientById(patient.getId());
        int patientListAfterSize = patientDao.findAllPatients().size();

        Assertions.assertEquals(expectedValue, deleteResult);
        Assertions.assertEquals(expectedSize, patientListAfterSize);
    }

    @Test
    void findPatientById(){
        PatientDTO patientDTO = new PatientDTO();
        patientDTO.setId(1L);
        Long expectedId = 1L;
        boolean expectedValue = true;
        Optional<PatientDTO> patient = patientDao.findPatientById(expectedId);
        Assertions.assertEquals(expectedValue, patient.isPresent());
        patient.ifPresent(value -> Assertions.assertEquals(expectedId, patientDTO.getId()));
    }

}
