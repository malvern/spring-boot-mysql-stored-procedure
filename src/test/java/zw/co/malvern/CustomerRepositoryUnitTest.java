package zw.co.malvern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import zw.co.malvern.repository.api.CustomerRepository;
import zw.co.malvern.repository.impl.CustomerRepositoryImpl;
import zw.co.malvern.util.dto.ProcedureDto;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
public class CustomerRepositoryUnitTest {
    private CustomerRepository customerRepository;

    @Autowired
    private  JdbcTemplate jdbcTemplate;

    @Autowired
    private  Environment environment;


    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl(jdbcTemplate,environment);
    }

    @Test
    @DisplayName("Basic : create find all customer procedure")
    void createFindAllCustomersShouldCreateStoredProcedureSuccessfully() {
        customerRepository.createCustomerRetreiveProcedure();
    }

    @Test
    @DisplayName("IN : create find  customer by age procedure")
    void createFindCustomersByAgeShouldCreateStoredProcedureSuccessfully() {
        customerRepository.createFindCustomerByAgeProcedure();
    }


    @Test
    @DisplayName("OUT: create summing promotional points procedure")
    void createSumPromotionalPoinsShouldCreateStoredProcedureSuccessfully() {
        customerRepository.createSumAllPointsProcedure();
    }

    @Test
    @DisplayName("INOUT : create summing promotional points procedure")
    void createFindCustomberBuSurnameShouldCreateStoredProcedureSuccessfully() {
        customerRepository.findCustomerBySurname();
    }


    @Test
    @DisplayName("IN and OUT create customer age by name ")
    void createFindCustomberAgeByNameBuSurnameShouldCreateStoredProcedureSuccessfully() {
        customerRepository.createFindCustomerAgeByNameProcedure();
    }

    @Test
    @DisplayName("Deleting stored procedure")
    void deleteStoredProcedureShouldDeleteStoredProcedureSuccessfully(){
        final String procedureName = "findAllCustomers";
        customerRepository.deleteStoredProcedures(procedureName);
    }

    @Test
    @DisplayName("Altering stored procedure")
    void alteringStoredProcedureShouldDeleteStoredProcedureSuccessfully(){
        customerRepository.alterCustomerRetreiveProcedure();
    }

    @Test
    @DisplayName("retreive customer age using name")
    void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAge(){
        final String name = "Tanaka";
        final Long age = customerRepository.findCustomerByAge(name);
        assertThat(age).as("Customer Age").isNotNull();
        assertThat(age).as("Age").isEqualTo(45L);
    }
    @Test
    @DisplayName("retreive procedure information")
    void viewProcedureInformationShouldReturnProcedureInformation() {
        final ProcedureDto procedureDto = customerRepository.viewStoredProceduresInformation();
        assertThat(procedureDto).as("procedure information object").isNotNull();
        assertThat(procedureDto.getRoutineSchema()).as("database where procedure is defined")
                .isEqualTo("kyc_database");
        assertThat(procedureDto.getName()).as("procedure name").isEqualTo("findAllCustomers");
        assertThat(procedureDto.getRoutineBodyDefination()).as("routine body definition(query)")
                .isEqualTo("BEGIN Select * from customer; END");
        assertThat(procedureDto.getRoutineBodyLanguage()).as("routine body language").isEqualTo("SQL");
        assertThat(procedureDto.getDateCreated()).as("date when procedure was created")
                .isEqualTo("2020-07-25 18:37:40");
        assertThat(procedureDto.getComment()).as("procedure comment")
                .isEqualTo("This procedure returns all customers");
        assertThat(procedureDto.getDefiner()).as("procedure definer").isEqualTo("root@%");
        assertThat(procedureDto.getDeterminstic()).as("determinstic").isEqualTo("NO");
        assertThat(procedureDto.getLastAltered()).as("date when procedure was altered")
                .isEqualTo("2020-07-25 18:37:39");
        assertThat(procedureDto.getSecurityType()).as("security type").isEqualTo(null);
        assertThat(procedureDto.getSqlMode()).as("sql mode").isEqualTo("INVOKER");

    }
}
