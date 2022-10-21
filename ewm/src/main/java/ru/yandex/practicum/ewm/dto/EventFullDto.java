package ru.yandex.practicum.ewm.dto;

import lombok.Data;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.model.Location;

import java.time.LocalDateTime;

@Data
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private Long views;
}
