package ru.yandex.practicum.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ViewStats {
    private String app;
    private String uri;
    private long hits;
}
