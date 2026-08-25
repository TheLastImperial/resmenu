package com.thelastimperial.resmenu.controllers.rs;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ProductRs {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private UUID imageId;
}
