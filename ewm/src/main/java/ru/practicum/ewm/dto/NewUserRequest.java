package ru.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Data
public class NewUserRequest {
    @NotBlank(message = "{name.notblank}")
    private String name;
    @NotEmpty(message = "{email.notempty}")
    @Email(message = "{email.valid}")
    private String email;
}
