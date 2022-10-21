package ru.yandex.practicum.ewm.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Data
public class NewCompilationDto {
    @NotBlank(message = "{title.notblank}")
    String title;
    @NotNull(message = "{pinned.notnull}")
    Boolean pinned;
    @NotEmpty(message = "{events.notempty}")
    List<Long> events = new ArrayList<>();
}
