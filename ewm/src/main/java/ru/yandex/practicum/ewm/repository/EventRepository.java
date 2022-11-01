package ru.yandex.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.format.annotation.DateTimeFormat;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventState;
import ru.yandex.practicum.ewm.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EventRepository extends JpaRepository<Event, Long> {

    String event = "NEW ru.yandex.practicum.ewm.model.Event(" +
            "ev.id," +
            "ev.title," +
            "ev.annotation," +
            "ev.description," +
            "ev.category," +
            "ev.confirmedRequests," +
            "ev.createdOn," +
            "ev.eventDate," +
            "ev.initiator," +
            "ev.location," +
            "ev.paid," +
            "ev.participantLimit," +
            "ev.publishedOn," +
            "ev.requestModeration," +
            "ev.state," +
            "ev.views," +
            "COALESCE((SELECT COUNT(evl.userId) FROM EventLike evl WHERE evl.positive = TRUE GROUP BY evl.eventId), 0) - " +
            "COALESCE((SELECT COUNT(evl.userId) FROM EventLike evl WHERE evl.positive = FALSE GROUP BY evl.eventId), 0)) ";

    @Query("SELECT " + event +
            "FROM Event ev WHERE " +
            "(FALSE = :onlyAvailable OR (ev.confirmedRequests < ev.participantLimit) AND TRUE = :onlyAvailable " +
            "OR ev.participantLimit = 0) " +
            "AND ev.category.id IN :categories  " +
            "AND ev.state = 'PUBLISHED' " +
            "AND ev.paid = :paid " +
            "AND (ev.eventDate >= :rangeStart) " +
            "AND (ev.eventDate <= :rangeEnd) " +
            "AND (UPPER(ev.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(ev.description) LIKE UPPER(CONCAT('%',:text,'%'))) "
    )
    List<Event> searchEventByText(String text,
                                  List<Long> categories,
                                  boolean paid,
                                  LocalDateTime rangeStart,
                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime rangeEnd,
                                  boolean onlyAvailable,
                                  Pageable page);

    @Query("SELECT " + event + "FROM Event ev WHERE ev.id = :id AND ev.state = :state")
    Event fiEventByIdAndState(long id, EventState state);

    @Query("SELECT " + event + "FROM Event ev WHERE ev.initiator = :initiator")
    List<Event> findEventsByInitiator(User initiator, Pageable page);

    @Query("SELECT " + event + "FROM Event ev WHERE " +
            "ev.initiator.id IN :users  " +
            "AND ev.state IN :states  " +
            "AND ev.category.id IN :categories  " +
            "AND (ev.eventDate >= :rangeStart) " +
            "AND (ev.eventDate <= :rangeEnd) "
    )
    List<Event> getEventsAdmin(List<Long> users,
                               List<Long> categories,
                               List<EventState> states,
                               LocalDateTime rangeStart,
                               LocalDateTime rangeEnd,
                               Pageable page);

    @Query("SELECT " + event + "FROM Event ev WHERE ev.id IN :events")
    Set<Event> getEventsForCompilation(List<Long> events);
}