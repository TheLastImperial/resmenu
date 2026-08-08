package com.thelastimperial.resmenu.controllers.validation;

import java.util.Optional;

import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.services.UserService;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UsernameExistsValidator implements ConstraintValidator<UsernameExists, String> {
    private final UserService userService;

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        Optional<UserEntity> user = userService.getByUsername(value);
        return user.isEmpty();
    }
}
