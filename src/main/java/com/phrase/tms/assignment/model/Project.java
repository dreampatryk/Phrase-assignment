package com.phrase.tms.assignment.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Project {
    private String name;
    private String status;
    private String sourceLang;
    private List<String> targetLangs;
}
