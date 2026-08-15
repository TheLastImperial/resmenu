package com.thelastimperial.resmenu.controllers.rs;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class SectionRs {
    private Long id;
    private String name;
    private String description;
    private Long menuId;
    private List<ProductRs> products = new ArrayList<>();
}
