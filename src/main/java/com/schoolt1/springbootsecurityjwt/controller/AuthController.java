package com.schoolt1.springbootsecurityjwt.controller;
import com.schoolt1.springbootsecurityjwt.dto.JwtAuthenticationResponse;
import com.schoolt1.springbootsecurityjwt.dto.SignInRequest;
import com.schoolt1.springbootsecurityjwt.dto.SignUpRequest;
import com.schoolt1.springbootsecurityjwt.service.AuthenticationService;
import com.schoolt1.springbootsecurityjwt.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name = "Вход/регистрация")
public class AuthController {

    private final AuthenticationService authenticationService;

    private final UserService service;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    @SecurityRequirements()
    public JwtAuthenticationResponse signUp(@RequestBody @Valid SignUpRequest request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    @SecurityRequirements()
    public JwtAuthenticationResponse signIn(@RequestBody @Valid SignInRequest request) {
        return authenticationService.signIn(request);
    }

    @GetMapping("/get-admin")
    @Operation(summary = "Получить роль ADMIN (для демонстрации)")
    public void getAdmin() {
        service.getAdmin();
    }
}