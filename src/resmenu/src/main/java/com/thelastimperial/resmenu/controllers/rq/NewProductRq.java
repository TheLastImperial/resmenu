package com.thelastimperial.resmenu.controllers.rq;

import java.util.UUID;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewProductRq {
    private String name;
    private String description;
    private Double price;
    private Long menuId;
    private Long sectionId;
    private MultipartFile image;
    private UUID imageId;
}
