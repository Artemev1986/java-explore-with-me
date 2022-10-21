package ru.yandex.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT ev FROM Event ev WHERE " +
            "(FALSE = :onlyAvailable OR (ev.confirmedRequests < ev.participantLimit) AND TRUE = :onlyAvailable " +
            "OR ev.participantLimit = 0) " +
            "AND ev.category.id IN :categories  " +
            "AND ev.state = 'PUBLISHED' " +
            "AND ev.paid = :paid " +
            "AND (:rangeStart IS NOT NULL AND ev.eventDate >= :rangeStart OR :rangeStart IS NULL AND ev.eventDate >= CURRENT_TIMESTAMP) " +
            "AND (:rangeEnd IS NOT NULL AND ev.eventDate <= :rangeEnd  OR :rangeEnd IS NULL) " +
            "AND (UPPER(ev.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(ev.description) LIKE UPPER(CONCAT('%',:text,'%'))) "
    )
    List<Event> searchEventByText(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  Pageable page);

    Event findEventByIdAndState(long id, EventState state);

    List<Event> findEventsByInitiator(User initiator, Pageable page);

    @Query("SELECT ev FROM Event ev WHERE " +
            "ev.initiator.id IN :users  " +
            "AND ev.state IN :states  " +
            "AND ev.category.id IN :categories  " +
            "AND (:rangeStart IS NOT NULL AND ev.eventDate >= :rangeStart OR :rangeStart IS NULL AND ev.eventDate >= CURRENT_TIMESTAMP) " +
            "AND (:rangeEnd IS NOT NULL AND ev.eventDate <= :rangeEnd  OR :rangeEnd IS NULL) "
    )
    List<Event> getEventsAdmin(List<Long> users,
                               List<Long> categories,
                               List<EventState> states,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Pageable page);

    @Query("SELECT ev FROM Event ev WHERE ev.id IN :events")
    Set<Event> getEventsForCompilation(List<Long> events);
}