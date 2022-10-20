package ru.practicum.ewm.repository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component()
@RequiredArgsConstructor
public class EventCompilationDb implements EventCompilationRepository {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public void deleteEvent(long compilationId, long eventId) {
        jdbcTemplate.update("DELETE FROM event_compilations WHERE" +
                " event_id = ? AND compilation_id = ?;", compilationId, eventId);
    }
}
