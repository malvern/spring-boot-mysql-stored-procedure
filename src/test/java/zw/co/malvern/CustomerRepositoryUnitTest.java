package zw.co.malvern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import zw.co.malvern.repository.api.CustomerRepository;
import zw.co.malvern.repository.impl.CustomerProcedureRepositoryImpl;
import zw.co.malvern.repository.impl.CustomerRepositoryImpl;

import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class CustomerRepositoryUnitTest {
    private CustomerRepository customerRepository;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @BeforeEach
    void setUp() {
        customerRepository = new CustomerRepositoryImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("retreive customer age using name")
    void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAge() {
        final String name = "Tanaka";
        final Integer age = customerRepository.findCustomerByAge(name);
        assertThat(age).as("Customer Age").isNotNull();
        assertThat(age).as("Age").isEqualTo(45L);
    }

    @Test
    @DisplayName("retreive customer age using name(StoredProcedure)")
    void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAgeUsingMyStoredProcedure() {
        final String name = "Tanaka";
        final Integer age = customerRepository.findCustomerByAgeUsingStoredProcedure(name);
        assertThat(age).as("Customer Age").isNotNull();
        assertThat(age).as("Age").isEqualTo(45L);
    }

    @Test
    @DisplayName("retreive customer age using name(MyStoredProcedure)")
    void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAgeUsingCallableStatement() throws SQLException {
        final String name = "Tanaka";
        final Integer age = customerRepository.findCustomerByAgeUsingCallableStatement(name);
        assertThat(age).as("Customer Age").isNotNull();
        assertThat(age).as("Age").isEqualTo(45L);
    }

    @Test
    @DisplayName("retreive customer age using name(JdbcTemplate)")
    void givenCustomerNameWhenfindCustomerByAgeShouldReturnCustomerAgeUsingJdbcTemplate() throws SQLException {
        final String name = "Tanaka";
        final Integer age = customerRepository.findCustomerByAgeUsingJdbcStatement(name);
        assertThat(age).as("Customer Age").isNotNull();
        assertThat(age).as("Age").isEqualTo(45L);
    }

}
