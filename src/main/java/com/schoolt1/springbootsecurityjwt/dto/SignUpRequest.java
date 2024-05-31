package com.schoolt1.springbootsecurityjwt.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Запрос на регистрацию")
@Data
@Builder
@Getter
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Anton")
    @Size(min = 5, max = 30, message = "Имя пользователя должно содержать от 5 до 30 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "my_1secret1_password")
    @Size(min = 4,max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

    @Schema(description = "Адрес электронной почты", example = "tonanton@gmail.com")
    @Size(min = 5, max = 255, message = "Адрес электронной почты должен содержать от 5 до 255 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустым")
    @Email(message = "Email - адрес должен быть в формате user@test.com")
    private String email;

}
