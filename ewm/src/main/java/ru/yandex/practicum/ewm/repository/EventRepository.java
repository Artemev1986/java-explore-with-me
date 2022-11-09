package ru.yandex.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.model.User;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT ev FROM Event ev WHERE " +
            "(FALSE = :onlyAvailable OR (ev.confirmedRequests < ev.participantLimit) AND TRUE = :onlyAvailable " +
            "OR ev.participantLimit = 0) " +
            "AND (:categories IS NULL OR ev.category.id IN :categories AND :categories IS NOT NULL)  " +
            "AND ev.state = 'PUBLISHED' " +
            "AND (:paid IS NULL OR ev.paid = :paid AND :paid IS NOT NULL) " +
            "AND (ev.eventDate >= :rangeStart) " +
            "AND (ev.eventDate <= :rangeEnd) " +
            "AND (:text IS NULL OR (UPPER(ev.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(ev.description) LIKE UPPER(CONCAT('%',:text,'%'))) AND :text IS NOT NULL) "
    )
    List<Event> searchEventByText(String text,
                                  List<Long> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  Pageable page);

    Event findEventByIdAndState(long id, EventState state);

    List<Event> findEventsByInitiator(User initiator, Pageable page);

    @Query("SELECT ev FROM Event ev WHERE " +
            "(:users IS NULL OR ev.initiator.id IN :users  AND :users IS NOT NULL) " +
            "AND (:states IS NULL OR ev.state IN :states AND :states IS NOT NULL) " +
            "AND (:categories IS NULL OR ev.category.id IN :categories AND :categories IS NOT NULL)  " +
            "AND (ev.eventDate >= :rangeStart) " +
            "AND (ev.eventDate <= :rangeEnd) "
    )
    List<Event> getEventsAdmin(List<Long> users,
                               List<Long> categories,
                               List<EventState> states,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Pageable page);

    @Query("SELECT ev FROM Event ev WHERE ev.id IN :events")
    Set<Event> getEventsForCompilation(List<Long> events);

    default Event getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("The event with id (" + id + ") not found"))
        );
    }
}