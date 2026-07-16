package com.thelastimperial.resmenu.controllers.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EditProductRq {
    private String name;
    private String description;
    private Double price;
}
