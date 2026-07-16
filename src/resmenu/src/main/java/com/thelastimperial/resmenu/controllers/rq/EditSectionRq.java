package com.thelastimperial.resmenu.controllers.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EditSectionRq {
    private String name;
    private Long menuId;
}
