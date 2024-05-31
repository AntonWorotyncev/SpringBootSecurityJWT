package com.schoolt1.springbootsecurityjwt.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/resource")
@RequiredArgsConstructor
@Tag(name = "Ресурсы", description = "Примеры запросов с разными правами доступа")
public class ContentController {
    private static String SECRET_CONTENT = "Secret content";

    private static String ADMIN_SECRET_CONTENT = "Admin secret content";

    @GetMapping
    @Operation(summary = "Доступен только авторизованным пользователям")
    public String getSecretContent() {
        return SECRET_CONTENT;
    }

    @GetMapping("/admin")
    @Operation(summary = "Доступен только авторизованным пользователям с ролью ADMIN")
    public String getAdminSecretContent() {
        return ADMIN_SECRET_CONTENT;
    }
}