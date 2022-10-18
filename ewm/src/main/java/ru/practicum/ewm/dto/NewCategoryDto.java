package ru.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class NewCategoryDto {
    @NotBlank(message = "{name.notblank}")
    private String name;
}
