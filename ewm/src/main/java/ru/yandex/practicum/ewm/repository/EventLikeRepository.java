package ru.yandex.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ewm.model.EventLike;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

    @Query("SELECT COALESCE(COUNT(evl.userId), 0) FROM EventLike evl WHERE " +
            "evl.positive = TRUE " +
            "AND evl.eventId = :eventId " +
            "GROUP BY evl.eventId")
    Long getLikes(Long eventId);

    @Query("SELECT COALESCE(COUNT(evl.userId), 0) FROM EventLike evl WHERE " +
            "evl.positive = FALSE " +
            "AND evl.eventId = :eventId " +
            "GROUP BY evl.eventId")
    Long getDislikes(Long eventId);
}