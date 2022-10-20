package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.ParticipationRequest;

import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {

    List<ParticipationRequest> findAllByEventId(long eventId);
    ParticipationRequest findParticipationRequestByIdAndEventId(long requestId, long eventId);

    @Modifying(clearAutomatically = true)
    @Query("update ParticipationRequest req set req.status = 'REJECTED' where " +
            "req.eventId = :eventId AND req.status = 'PENDING'")
    long updateEventRequests(long eventId);
}
