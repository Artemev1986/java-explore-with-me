package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Data
public class AdminUpdateEventRequest {
    private String title;
    private String annotation;
    private String description;
    private Long category;
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
}
