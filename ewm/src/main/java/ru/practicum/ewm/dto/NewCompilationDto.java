package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Data
public class NewCompilationDto {
    @NotBlank(message = "{title.notblank}")
    String title;
    @NotNull(message = "{pinned.notnull}")
    Boolean pinned;
    @NotEmpty(message = "{events.notempty}")
    Set<Event> events = new HashSet<>();
}
