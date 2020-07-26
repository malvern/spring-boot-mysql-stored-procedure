package zw.co.malvern.repository.impl;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.object.StoredProcedure;


public class CustomStoredProcedure extends StoredProcedure {
    public CustomStoredProcedure(JdbcTemplate jdbcTemplate, String name) {
        super(jdbcTemplate, name);
        setFunction(false);
    }
}
