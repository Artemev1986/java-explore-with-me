package ru.yandex.practicum.ewm.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.ewm.model.Location;

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
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE, pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
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
