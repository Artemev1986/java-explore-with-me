package ru.yandex.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewm.model.EventLike;

public interface EventLikeRepository extends JpaRepository<EventLike, Long> {

    EventLike findEventLikeByUserIdAndEventId(Long userId, Long eventId);
}