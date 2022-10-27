package ru.yandex.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.stats.model.EndpointHit;
import ru.yandex.practicum.stats.model.ViewStats;
import ru.yandex.practicum.stats.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatsService {

    private final StatsRepository statsRepository;

    public void addHit(EndpointHit hit) {
        statsRepository.save(hit);
        log.debug("Added new hit with id: {}", hit.getId());
    }

    public List<ViewStats> getStats(LocalDateTime start,
                                    LocalDateTime end,
                                    List<String> uris,
                                    Boolean unique) {
        List<ViewStats> viewStats;

        if (unique) {
            viewStats = statsRepository.getDistinctStats(start, end, uris);
        } else {
            viewStats = statsRepository.getStats(start, end, uris);
        }
        log.debug("Get stats. Stats counts {}", viewStats.size());
        return viewStats;
    }
}
