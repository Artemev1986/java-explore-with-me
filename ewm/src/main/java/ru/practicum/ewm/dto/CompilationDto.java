package ru.practicum.ewm.dto;

import lombok.Data;
import ru.practicum.ewm.model.Event;

import java.util.ArrayList;
import java.util.List;

@Data
public class CompilationDto {
    private Long id;
    private String title;
    private Boolean pinned;
    private List<Event> events = new ArrayList<>();
}
