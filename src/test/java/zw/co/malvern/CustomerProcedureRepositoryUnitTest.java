package zw.co.malvern;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import zw.co.malvern.repository.api.CustomerProcedureRepository;
import zw.co.malvern.repository.impl.CustomerProcedureRepositoryImpl;
import zw.co.malvern.util.dto.ProcedureDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
 class CustomerProcedureRepositoryUnitTest {

    private CustomerProcedureRepository customerProcedureRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private Environment environment;


    @BeforeEach
    void setUp() {
        customerProcedureRepository = new CustomerProcedureRepositoryImpl(jdbcTemplate, environment);
    }

    @Test
    @DisplayName("Basic : create find all customer procedure")
    @Order(1)
    void createFindAllCustomersShouldCreateStoredProcedureSuccessfully() {
        customerProcedureRepository.createCustomerRetreiveProcedure();
    }

    @Test
    @DisplayName("IN : create find  customer by age procedure")
    @Order(2)
    void createFindCustomersByAgeShouldCreateStoredProcedureSuccessfully() {
        customerProcedureRepository.createFindCustomerByAgeProcedure();
    }


    @Test
    @DisplayName("OUT: create summing promotional points procedure")
    @Order(3)
    void createSumPromotionalPoinsShouldCreateStoredProcedureSuccessfully() {
        customerProcedureRepository.createSumAllPointsProcedure();
    }

    @Test
    @DisplayName("INOUT : create summing promotional points procedure")
    @Order(4)
    void createFindCustomberBuSurnameShouldCreateStoredProcedureSuccessfully() {
        customerProcedureRepository.findCustomerBySurname();
    }


    @Test
    @DisplayName("IN and OUT create customer age by name ")
    @Order(5)
    void createFindCustomberAgeByNameBuSurnameShouldCreateStoredProcedureSuccessfully() {
        customerProcedureRepository.createFindCustomerAgeByNameProcedure();
    }

    @Test
    @DisplayName("Deleting stored procedure")
    @Order(8)
    void deleteStoredProcedureShouldDeleteStoredProcedureSuccessfully() {
        final String procedureName = "findAllCustomers";
        customerProcedureRepository.deleteStoredProcedures(procedureName);
    }

    @Test
    @DisplayName("Altering stored procedure")
    @Order(6)
    void alteringStoredProcedureShouldDeleteStoredProcedureSuccessfully() {
        customerProcedureRepository.alterCustomerRetreiveProcedure();
    }

    @Test
    @DisplayName("retreive procedure information")
    @Order(7)
    void viewProcedureInformationShouldReturnProcedureInformation() {
        final ProcedureDto procedureDto = customerProcedureRepository.viewStoredProceduresInformation();
        assertThat(procedureDto).as("procedure information object").isNotNull();
        assertThat(procedureDto.getRoutineSchema()).as("database where procedure is defined")
                .isEqualTo("kyc_database");
        assertThat(procedureDto.getName()).as("procedure name").isEqualTo("findAllCustomers");
        assertThat(procedureDto.getRoutineBodyDefination()).as("routine body definition(query)")
                .isEqualTo("BEGIN SELECT * FROM customer; END");
        assertThat(procedureDto.getRoutineBodyLanguage()).as("routine body language").isEqualTo("SQL");
        assertThat(procedureDto.getDateCreated()).as("date when procedure was created")
                .isNotNull();
        assertThat(procedureDto.getComment()).as("procedure comment")
                .isEqualTo("This procedure returns all customers");
        assertThat(procedureDto.getDefiner()).as("procedure definer").isEqualTo("root@%");
        assertThat(procedureDto.getDeterminstic()).as("determinstic").isEqualTo("NO");
        assertThat(procedureDto.getLastAltered()).as("date when procedure was altered")
                .isNotNull();
        assertThat(procedureDto.getSecurityType()).as("security type").isEqualTo(null);
        assertThat(procedureDto.getSqlMode()).as("sql mode").isEqualTo("INVOKER");

    }

}
