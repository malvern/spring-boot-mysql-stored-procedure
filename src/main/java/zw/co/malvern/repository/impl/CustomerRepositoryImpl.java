package zw.co.malvern.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.stereotype.Service;
import zw.co.malvern.repository.api.CustomerRepository;
import zw.co.malvern.util.converter.ResultConverter;
import zw.co.malvern.util.dto.ProcedureDto;

import java.util.Arrays;

import static zw.co.malvern.util.converter.ResultConverter.convertMysqlResultToProcedureDto;

@Service
public class CustomerRepositoryImpl implements CustomerRepository {
    private Logger LOGGER = LoggerFactory.getLogger(CustomerRepositoryImpl.class);


    private final JdbcTemplate jdbcTemplate;

    private final Environment environment;

    public CustomerRepositoryImpl(JdbcTemplate jdbcTemplate, Environment environment) {
        this.jdbcTemplate = jdbcTemplate;
        this.environment = environment;
    }

    @Override
    public void deleteStoredProcedures() {
        LOGGER.info("deleting all previously created procedures");
        Arrays.asList("findAllCustomers", "findCustomerAgeByName","findCustomerCountByAge","findCustomberByName",
                "findCustomberBySurname","sumPromotionalPoints").
                forEach(name -> {
                    final String queryDrop = "DROP PROCEDURE IF EXISTS " + name;
                    jdbcTemplate.execute(queryDrop);
                });
        LOGGER.info("all procedures successfully deleted");
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
        final String query = environment.getProperty("customer.procedure.count.by.age");
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

    public void findCustomerByName(){
        final String query =  environment.getProperty("customer.procedure.by.name");
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