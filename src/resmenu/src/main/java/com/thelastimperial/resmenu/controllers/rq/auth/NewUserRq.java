package com.thelastimperial.resmenu.controllers.rq.auth;

import com.thelastimperial.resmenu.controllers.validation.EqualsStrings;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@EqualsStrings(
    field = "password",
    fieldMatch = "passwordConfirmation",
    message = "Password doesn't match."
)
public class NewUserRq {
    @Pattern(
        regexp="^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$",
        message = "Bad email pattern."
    )
    private String username;
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = "8 characters, one uppercase letter, one lowercase letter, one number, and one special character(@$!%*?&)."
    )
    private String password;
    private String passwordConfirmation;
}
