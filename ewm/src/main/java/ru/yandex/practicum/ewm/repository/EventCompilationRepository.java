package ru.yandex.practicum.ewm.repository;

public interface EventCompilationRepository {

    void deleteEvent(long compilationId, long eventId);
}
