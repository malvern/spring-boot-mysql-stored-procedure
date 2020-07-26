package zw.co.malvern.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import zw.co.malvern.repository.api.CustomerProcedureRepository;
import zw.co.malvern.util.dto.ProcedureDto;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

import static zw.co.malvern.util.converter.ResultConverter.convertMysqlResultToProcedureDto;

@Service
public class CustomerProcedureRepositoryImpl implements CustomerProcedureRepository {
    private Logger LOGGER = LoggerFactory.getLogger(CustomerProcedureRepositoryImpl.class);


    private final JdbcTemplate jdbcTemplate;

    private final Environment environment;

    public CustomerProcedureRepositoryImpl(JdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }


    @Override
    public void deleteStoredProcedures(String procedureName) {
        final String queryDrop = "DROP PROCEDURE IF EXISTS " + procedureName;
        jdbcTemplate.execute(queryDrop);

    }



    @Override
    public void createCustomerRetreiveProcedure() {
        final String query = environment.getProperty("customer.procedure.find.all");
        jdbcTemplate.execute(query);
    }

    @Override
    public void createFindCustomerByAgeProcedure() {
        //returns ResultSet
        //IN parameter
        final String query = environment.getProperty("customer.procedure.find.by.age");
        jdbcTemplate.execute(query);
    }

    public void createSumAllPointsProcedure(){
        //OUT procedure
        final String query = environment.getProperty("customer.procedure.sum.promotion.points");
        jdbcTemplate.execute(query);
    }

    @Override
    public void createFindCustomerAgeByNameProcedure() {
        //OUT and IN
        final String query = environment.getProperty("customer.procedure.find.age.by.name");
        jdbcTemplate.execute(query);
    }


    public void findCustomerBySurname(){
        final String query =  environment.getProperty("customer.procedure.by.surname");
        jdbcTemplate.execute(query);
    }

    public void alterCustomerRetreiveProcedure(){
        final String query =  environment.getProperty("customer.procedure.alter.find.all");
        jdbcTemplate.execute(query);
    }

    @Override
    public ProcedureDto viewStoredProceduresInformation() {
        final String query =  environment.getProperty("view.procedure.information");
        return jdbcTemplate.queryForObject(query, (rs,num)->convertMysqlResultToProcedureDto(rs));
    }


}