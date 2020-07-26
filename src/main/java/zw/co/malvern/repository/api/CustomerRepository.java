package zw.co.malvern.repository.api;

import zw.co.malvern.util.dto.ProcedureDto;

import java.sql.SQLException;

public interface CustomerRepository {

    Integer findCustomerByAge(String name);

    Integer findCustomerByAgeUsingStoredProcedure(String name);

    Integer findCustomerByAgeUsingCallableStatement(String name) throws SQLException;

    Integer findCustomerByAgeUsingJdbcStatement(String name) throws SQLException;


}
