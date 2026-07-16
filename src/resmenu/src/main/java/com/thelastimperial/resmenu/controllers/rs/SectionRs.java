package com.thelastimperial.resmenu.controllers.rs;

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
    private Long menuId;
}
