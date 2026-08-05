package com.thelastimperial.resmenu.controllers.rq.auth;

import java.util.UUID;

import groovy.transform.builder.Builder;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class NewPasswordRq {
    private String password;
    private String passwordConfirmation;
    private UUID token;
}
