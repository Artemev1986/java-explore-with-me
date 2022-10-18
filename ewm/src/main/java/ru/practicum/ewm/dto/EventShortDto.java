package ru.practicum.ewm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventShortDto {
    private Long id;
    private String title;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Boolean paid;
    private Long views;
}
