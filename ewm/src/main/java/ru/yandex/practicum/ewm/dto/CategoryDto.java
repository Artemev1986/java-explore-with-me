package ru.yandex.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CategoryDto {
    @NotNull(message = "{idx.null}")
    private Long id;
    @NotBlank(message = "{name.notblank}")
    private String name;
}
