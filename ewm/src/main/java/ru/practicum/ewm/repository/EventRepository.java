package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT ev FROM Event ev WHERE " +
            "(FALSE = :onlyAvailable OR (ev.confirmedRequests < ev.participantLimit) AND TRUE = :onlyAvailable) " +
            "AND ev.category in :categories  " +
            "AND ev.state = 'PUBLISHED' " +
            "AND ev.paid = :paid " +
            "AND ev.eventDate >= :rangeStart " +
            "AND ev.eventDate <= :rangeEnd " +
            "AND (UPPER(ev.annotation) LIKE UPPER(CONCAT('%',:text,'%')) " +
            "OR UPPER(ev.description) LIKE UPPER(CONCAT('%',:text,'%'))) " +
            "ORDER BY ev.views DESC")
    List<Event> searchEventByText(String text,
                                  List<Category> categories,
                                  Boolean paid,
                                  LocalDateTime rangeStart,
                                  LocalDateTime rangeEnd,
                                  Boolean onlyAvailable,
                                  Pageable page);
}
