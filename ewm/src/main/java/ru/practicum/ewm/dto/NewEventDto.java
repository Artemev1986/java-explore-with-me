package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Data
public class NewEventDto {
    @NotBlank(message = "{title.notblank}")
    private String title;
    @NotNull(message = "{annotation.notnull}")
    @Size(min = 20, max = 2000, message = "{annotation.size}")
    private String annotation;
    @NotNull(message = "{description.notnull}")
    @Size(min = 20, max = 7000, message = "{description.size}")
    private String description;
    @NotNull(message = "{category.notnull}")
    private Long category;
    @NotNull(message = "{eventDate.notnull}")
    @Future(message = "{eventDate.future}")
    private LocalDateTime eventDate;
    @NotNull(message = "{location.notnull}")
    private Location location;
    @NotNull(message = "{paid.notnull}")
    private Boolean paid;
    @NotNull(message = "{participantLimit.notnull}")
    private Integer participantLimit;
    @NotNull(message = "{requestModeration.notnull}")
    private Boolean requestModeration;
}
