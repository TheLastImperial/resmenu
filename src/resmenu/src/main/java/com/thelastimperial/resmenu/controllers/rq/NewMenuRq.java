package com.thelastimperial.resmenu.controllers.rq;

import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewMenuRq {
    @Max(value=100)
    private String name;
    @Max(value=100)
    private String address;
    @Max(value=12)
    private String phone;
    @Max(value=100)
    private String description;
}
