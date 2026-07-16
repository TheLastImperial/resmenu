package com.thelastimperial.resmenu.controllers.rs;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ResProductRs {
    private Long id;
    private String name;
    private String description;
    private Double price;
}
