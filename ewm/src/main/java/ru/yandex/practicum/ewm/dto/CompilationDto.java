package ru.yandex.practicum.ewm.dto;

import lombok.Data;
import ru.yandex.practicum.ewm.model.Event;

import java.util.HashSet;
import java.util.Set;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private Set<Event> events = new HashSet<>();
}
