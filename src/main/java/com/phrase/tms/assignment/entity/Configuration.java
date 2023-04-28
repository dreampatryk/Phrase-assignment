package com.phrase.tms.assignment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Configuration {
    @Id
    private String username;
    private String password;
    private boolean active;
}
