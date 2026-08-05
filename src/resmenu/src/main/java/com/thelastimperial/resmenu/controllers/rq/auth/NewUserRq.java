package com.thelastimperial.resmenu.controllers.rq.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class NewUserRq {
    private String username;
    private String password;
    private String passwordConfirmation;
}
