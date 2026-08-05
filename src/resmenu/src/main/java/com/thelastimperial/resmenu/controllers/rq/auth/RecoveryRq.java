package com.thelastimperial.resmenu.controllers.rq.auth;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class RecoveryRq {
    private String username;
}
