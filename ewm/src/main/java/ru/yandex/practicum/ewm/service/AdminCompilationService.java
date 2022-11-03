package ru.yandex.practicum.ewm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.yandex.practicum.ewm.dto.CompilationDto;
import ru.yandex.practicum.ewm.dto.NewCompilationDto;
import ru.yandex.practicum.ewm.mapper.CompilationMapper;
import ru.yandex.practicum.ewm.model.Compilation;
import ru.yandex.practicum.ewm.model.Event;
import ru.yandex.practicum.ewm.model.EventCompilation;
import ru.yandex.practicum.ewm.repository.CompilationRepository;
import ru.yandex.practicum.ewm.repository.EventCompilationRepository;
import ru.yandex.practicum.ewm.repository.EventRepository;

import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminCompilationService {

    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EventCompilationRepository eventCompilationRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        Set<Event> events = eventRepository.getEventsForCompilation(newCompilationDto.getEvents());
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto, events);
        compilationRepository.save(compilation);
        log.debug("Added new compilation with id: {}", compilation.getId());
        return CompilationMapper.toCompilationDto(compilation);
    }

    public void deleteCompilationById(long compilationId) {
        compilationRepository.getById(compilationId);
        compilationRepository.deleteById(compilationId);
        log.debug("The compilation with id: {} was deleted", compilationId);
    }

    @Transactional
    public void deleteEventFromCompilation(long compilationId, long eventId) {
        eventCompilationRepository.deleteEventCompilationByCompilationIdAndEventId(compilationId, eventId);
        log.debug("The event with id: {} was deleted from the compilation with id: {}", eventId, compilationId);
    }

    public void addEventToCompilation(long compilationId, long eventId) {
        compilationRepository.getById(compilationId);
        eventRepository.getById(eventId);
        EventCompilation eventCompilation = new EventCompilation();
        eventCompilation.setCompilationId(compilationId);
        eventCompilation.setEventId(eventId);
        eventCompilationRepository.save(eventCompilation);
        log.debug("The event with id: {} was added to the compilation with id: {}", eventId, compilationId);
    }

    public void unpinCompilation(long compilationId) {
        Compilation compilation = compilationRepository.getById(compilationId);
        compilation.setPinned(false);
        compilationRepository.save(compilation);
        log.debug("The compilation with id: {} was unpinned", compilationId);
    }

    public void pinCompilation(long compilationId) {
        Compilation compilation = compilationRepository.getById(compilationId);
        compilation.setPinned(true);
        compilationRepository.save(compilation);
        log.debug("The compilation with id: {} was pinned", compilationId);
    }
}
