package com.thelastimperial.resmenu.controllers.rs;

import java.util.ArrayList;
import java.util.List;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class ResSectionRs {
    private Long id;
    private String name;
    public ResSectionRs(){
        products = new ArrayList<>();
    }
    private List<ResProductRs> products;
}
