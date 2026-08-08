package com.thelastimperial.resmenu.controllers.rq.auth;

import com.thelastimperial.resmenu.controllers.validation.EqualsStrings;

import groovy.transform.builder.Builder;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
@EqualsStrings(
    field = "password",
    fieldMatch = "passwordConfirmation",
    message = "Password doesn't match."
)
public class NewPasswordRq {
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "8 characters, one uppercase letter, one lowercase letter, one number, and one special character(@$!%*?&)."
    )
    private String password;
    private String passwordConfirmation;
    private String token;
}
