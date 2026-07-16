package com.thelastimperial.resmenu.controllers.rq;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class EditMenuRq {
    private String name;
    private String address;
    private String phone;

    private boolean active;
}
