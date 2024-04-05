package ru.Ildar.AstonRest.dao;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Ildar.AstonREST.DAO.DoctorDAO;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;


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
class DoctorDAOTest {

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

    public final DoctorDAO doctorDAO = new DoctorDAO(dataSourceProperties);


    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void findAllDoctors() {
        List<DoctorDTO> doctors = doctorDAO.findAllDoctors();
        Assertions.assertEquals(3, doctors.size());
        DoctorDTO actual = doctors.get(0);
        Assertions.assertEquals("Mikhail", actual.getName());
    }

    @Test
    void saveDoctor() {
        String expectedName = "New name";
        String expectedSpecialization = "surgeon";

        DoctorDTO doctor = new DoctorDTO(
                expectedName,
                expectedSpecialization);
        doctorDAO.saveDoctor(doctor);

        Optional<DoctorDTO> resultDoctor = doctorDAO.findDoctorById(1L);

        Assertions.assertTrue(resultDoctor.isPresent());
    }

    @Test
    void updateDoctor() {

        String expectedName = "Update Name";

        DoctorDTO doctor = doctorDAO.findAllDoctors().get(1);
        doctor.setId(1L);
        doctor.setName(expectedName);


        doctorDAO.updateDoctor(doctor.getId(), doctor);
        Assertions.assertEquals(expectedName, doctor.getName());
    }

    @Test
    void deleteDoctorById() {
        Boolean expectedValue = true;
        int expectedSize = doctorDAO.findAllDoctors().size();

        DoctorDTO doctor = new DoctorDTO(
                4L,
                "Vladimir",
                "surgeon"
        );
        doctorDAO.saveDoctor(doctor);

        //   doctor.setId(4L);
        boolean deleteResult = doctorDAO.deleteDoctorById(doctor.getId());
        int doctorListAfterSize = doctorDAO.findAllDoctors().size();

        Assertions.assertEquals(expectedValue, deleteResult);
        Assertions.assertEquals(expectedSize, doctorListAfterSize);
    }

    @Test
    void findByPatientId() {
        DoctorDTO doctorDTO = new DoctorDTO();
        doctorDTO.setId(1L);
        Long expectedId = 1L;
        boolean expectedValue = true;
        Optional<DoctorDTO> doctor = doctorDAO.findDoctorById(expectedId);
        Assertions.assertEquals(expectedValue, doctor.isPresent());
        doctor.ifPresent(value -> Assertions.assertEquals(expectedId, doctorDTO.getId()));
    }

}
