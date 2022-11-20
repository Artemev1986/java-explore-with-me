package ru.yandex.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewm.model.EventCompilation;

public interface EventCompilationRepository extends JpaRepository<EventCompilation, Long> {

    void deleteEventCompilationByCompilationIdAndEventId(Long compilationId, Long eventId);
}