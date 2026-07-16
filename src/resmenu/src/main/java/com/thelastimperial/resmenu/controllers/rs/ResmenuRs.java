package com.thelastimperial.resmenu.controllers.rs;

import java.util.ArrayList;
import java.util.List;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ResmenuRs {
    private Long id;
    private String name;
    private String address;
    private String phone;
    private boolean active;

    public ResmenuRs(){
        sections = new ArrayList<>();
    }

    private List<ResSectionRs> sections;
}
