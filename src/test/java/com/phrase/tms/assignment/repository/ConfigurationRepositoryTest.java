package com.phrase.tms.assignment.repository;

import com.phrase.tms.assignment.entity.Configuration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class ConfigurationRepositoryTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void saveOrUpdate() {
        //given
        var username = "user";
        var password = "password";
        boolean active = true;

        var repository = new ConfigurationRepository(new NamedParameterJdbcTemplate(jdbcTemplate));

        //when
        repository.saveOrUpdate(username, password, active);

        //then
        List<Configuration> result = jdbcTemplate.query("SELECT * FROM CONFIGURATION", new BeanPropertyRowMapper<>(Configuration.class));
        assertEquals(result.size(), 1);
        var config = result.get(0);
        assertEquals(config.getUsername(), username);
        assertEquals(config.getPassword(), password);
        assertEquals(config.isActive(), active);

        //given
        var newPassword = "newPassword";

        //when
        repository.saveOrUpdate(username, newPassword, active);

        //then
        result = jdbcTemplate.query("SELECT * FROM CONFIGURATION", new BeanPropertyRowMapper<>(Configuration.class));
        assertEquals(result.size(), 1);
        config = result.get(0);
        assertEquals(config.getUsername(), username);
        assertEquals(config.getPassword(), newPassword);
        assertEquals(config.isActive(), active);
    }
}
