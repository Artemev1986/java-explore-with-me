package ru.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.model.Category;
import ru.practicum.ewm.model.Event;
import ru.practicum.ewm.model.ParticipationRequest;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.List;

public interface RequestRepository extends JpaRepository<ParticipationRequest, Long> {
}
