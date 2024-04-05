package ru.Ildar.AstonRest.dao;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.Ildar.AstonREST.DAO.DrugDAO;
import ru.Ildar.AstonREST.models.DTO.DoctorDTO;
import ru.Ildar.AstonREST.models.DTO.DrugDTO;

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
class DrugDAOTest {

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

    public final DrugDAO drugDAO = new DrugDAO(dataSourceProperties);


    @BeforeAll
    static void beforeAll() {
        container.start();
    }

    @AfterAll
    static void afterAll() {
        container.stop();
    }

    @Test
    void findAllDrugs() {
        List<DrugDTO> drugs = drugDAO.findAllDrugs();
        Assertions.assertEquals( 4, drugs.size());
        DrugDTO actual = drugs.get(0);
        Assertions.assertEquals("sleeping pills", actual.getTitle());
    }

    @Test
    void saveDrug() {
        String expectedTitle = "New title";

        DrugDTO drug = new DrugDTO(
            expectedTitle);
        drugDAO.saveDrug(drug);

        Optional<DrugDTO> resultDrug = drugDAO.findDrugById(1L);

        Assertions.assertTrue(resultDrug.isPresent());
    }

    @Test
    void updateDrug() {
        String expectedTitle = "Update title";

        DrugDTO drug = drugDAO.findAllDrugs().get(1);
        drug.setId(1L);
        drug.setTitle(expectedTitle);

        drugDAO.updateDrug(drug.getId(), drug);
        Assertions.assertEquals(expectedTitle, drug.getTitle());
    }

    @Test
    void deleteDrugById() {
        Boolean expectedValue = true;
        int expectedSize = drugDAO.findAllDrugs().size();

        DrugDTO drug = new DrugDTO(
            "Drug for delete title"
        );
        drugDAO.saveDrug(drug);

        boolean deleteResult = drugDAO.deleteDrugById(drug.getId());
        int doctorListAfterSize = drugDAO.findAllDrugs().size();

        Assertions.assertEquals(expectedValue, deleteResult);
        Assertions.assertEquals(expectedSize, doctorListAfterSize);
    }

    @Test
    void findByDrugId() {
        DrugDTO drugDTO = new DrugDTO();
        drugDTO.setId(1L);
        Long expectedId = 1L;
        boolean expectedValue = true;
        Optional<DrugDTO> drug = drugDAO.findDrugById(expectedId);
        Assertions.assertEquals(expectedValue, drug.isPresent());
        drug.ifPresent(value -> Assertions.assertEquals(expectedId, drugDTO.getId()));
    }
}
