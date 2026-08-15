package com.thelastimperial.resmenu.controllers.rs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MenuRs implements Serializable{
    private Long id;
    private String name;
    private String address;
    private String phone;
    private String description;
    private boolean active;
    private List<SectionRs> sections = new ArrayList<>();
}
