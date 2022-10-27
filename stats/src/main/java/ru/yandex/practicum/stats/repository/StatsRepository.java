package ru.yandex.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.stats.model.EndpointHit;
import ru.yandex.practicum.stats.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsRepository  extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT NEW ru.yandex.practicum.stats.model.ViewStats(hit.app, hit.uri, COUNT(DISTINCT hit.uri)) " +
            "FROM EndpointHit hit WHERE " +
            "hit.timestamp >= :start " +
            "AND hit.timestamp <= :end " +
            "AND hit.uri IN :uris " +
            "GROUP BY hit.app, hit.uri")
    List<ViewStats> getDistinctStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query("SELECT  NEW ru.yandex.practicum.stats.model.ViewStats(hit.app, hit.uri, COUNT(hit.uri)) " +
            "FROM EndpointHit hit WHERE " +
            "hit.timestamp >= :start " +
            "AND hit.timestamp <= :end " +
            "AND hit.uri IN :uris " +
            "GROUP BY hit.app, hit.uri")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}
