package zw.co.malvern.repository.impl;

import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import zw.co.malvern.repository.api.CustomerRepository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CustomerRepositoryImpl implements CustomerRepository {

    private final JdbcTemplate jdbcTemplate;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Integer findCustomerByAge(String name) {
        final SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("findCustomerAgeByName");
        final Map<String, String> inputParameters = new HashMap<>();
        inputParameters.put("name", name);
        inputParameters.put("age", "@age");
        final SqlParameterSource sqlParameterSource = new MapSqlParameterSource(inputParameters);
        final Integer result = jdbcCall.executeObject(Integer.class, sqlParameterSource);
        return result;
    }

    @Override
    public Integer findCustomerByAgeUsingStoredProcedure(String name) {
        final CustomStoredProcedure customStoredProcedure = new  CustomStoredProcedure(jdbcTemplate, "findCustomerAgeByName");
        final SqlParameter sqlName = new SqlParameter("name", Types.VARCHAR);
        final SqlOutParameter outParameter = new SqlOutParameter("age", Types.INTEGER);
        final SqlParameter[] sqlParameters = {sqlName,outParameter};
        customStoredProcedure.setParameters(sqlParameters);
        customStoredProcedure.setJdbcTemplate(jdbcTemplate);
        customStoredProcedure.compile();
        Map<String,Object> storedProcResult = customStoredProcedure.execute(name, "age");
        return (Integer)storedProcResult.get("age");
    }

    @Override
    public Integer findCustomerByAgeUsingCallableStatement(String name) throws SQLException {
        final Connection connection = jdbcTemplate.getDataSource().getConnection();
        final CallableStatement callableStatement = connection.prepareCall("{call findCustomerAgeByName(?, ?)}");
        callableStatement.setString(1, name);
        callableStatement.registerOutParameter(2, Types.INTEGER);
        callableStatement.executeUpdate();
        return callableStatement.getInt(2);
    }

    @Override
    public Integer findCustomerByAgeUsingJdbcStatement(String name) throws SQLException {
        final SqlParameter nameParameter = new SqlParameter(Types.VARCHAR);
        final SqlOutParameter outParameter = new SqlOutParameter("age", Types.INTEGER);
        final String callStoredProcedure = "{call findCustomerAgeByName(?, ?)}";
        Map<String, Object> resultMap = jdbcTemplate.call(connection -> {
            CallableStatement callableStatement = connection.prepareCall(callStoredProcedure);
            callableStatement.setString(1, name);
            callableStatement.registerOutParameter(2, Types.INTEGER);
            return callableStatement;
        }, Arrays.asList(nameParameter,outParameter));
        return (Integer)resultMap.get("age");
    }

}
