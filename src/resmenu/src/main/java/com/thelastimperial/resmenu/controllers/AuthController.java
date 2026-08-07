package com.thelastimperial.resmenu.controllers;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;

import com.thelastimperial.resmenu.controllers.rq.auth.NewPasswordRq;
import com.thelastimperial.resmenu.controllers.rq.auth.NewUserRq;
import com.thelastimperial.resmenu.controllers.rq.auth.RecoveryRq;
import com.thelastimperial.resmenu.entities.UserEntity;
import com.thelastimperial.resmenu.entities.UserRecoveryEntity;
import com.thelastimperial.resmenu.services.AuthService;
import com.thelastimperial.resmenu.services.UserService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@AllArgsConstructor
@Controller
@RequestMapping(path="/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/recovery")
    public String recovery(RecoveryRq recoveryRq) {
        return "auth/recovery";
    }
    
    @PostMapping("/recovery")
    public String sendRecovery(RecoveryRq recoveryRq, BindingResult bindingResult) {
        log.info("Received: {}", recoveryRq);
        if(bindingResult.hasErrors()){
            log.info("Errors: {}", bindingResult);
            return "auth/recovery";
        }
        UserRecoveryEntity recovery = authService.setRecovery(recoveryRq);
        boolean generated = recovery != null;
        return "redirect:/auth/recovery?generated=" + generated;
    }

    @GetMapping("/new_password/{token}")
    public String newPassword(@PathVariable UUID token, NewPasswordRq newPasswordRq, Model model) {
        Optional<UserRecoveryEntity> recovery = authService.getRecovery(token);
        if(recovery.isEmpty())
            return "auth/invalid-token";
        model.addAttribute("token", token);
        return "auth/new-password";
    }

    @PostMapping("/new_password")
    public String createNewPassword(NewPasswordRq newPasswordRq) {
        authService.createNewPassword(newPasswordRq);
        return "redirect:/login";
    }

    @GetMapping("/register")
    public String newUser(NewUserRq newUserRq) {
        return "auth/register";
    }

    @PostMapping("/register")
    public String createUser(@Valid NewUserRq newUserRq, BindingResult bindingResult) {
        if(bindingResult.hasErrors()){
            log.info("Errors: {}", bindingResult);
            return "auth/register";
        }
        Optional<UserEntity> user = userService.getByUsername(newUserRq.getUsername());
        if(user.isPresent()){
            bindingResult.rejectValue(
                "username", 
                "EmailAlreadyExists",
                "The Email already exists."
            );
            log.info("Error: {}", bindingResult);
            return "auth/register";
        }
        authService.createUser(newUserRq);
        return "redirect:/auth/login";
    }

}
