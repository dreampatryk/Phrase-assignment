package com.phrase.tms.assignment.repository;

import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@AllArgsConstructor
public class ConfigurationRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;

    public void saveOrUpdate(String userName, String password, boolean active) {
        var parameterSource = new MapSqlParameterSource("userName", userName)
                .addValue("password", password)
                .addValue("active", active);

        jdbcTemplate.update("INSERT INTO CONFIGURATION (USERNAME, PASSWORD, ACTIVE) values (:userName, :password, :active) ON DUPLICATE KEY UPDATE PASSWORD = :password", parameterSource);
    }
}
