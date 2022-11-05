package ru.yandex.practicum.ewm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.ewm.dto.CompilationDto;
import ru.yandex.practicum.ewm.dto.NewCompilationDto;
import ru.yandex.practicum.ewm.service.AdminCompilationService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/compilations")
public class AdminCompilationController {

    private final AdminCompilationService compilationService;

    @PostMapping
    public ResponseEntity<Object> addCompilation(@RequestBody NewCompilationDto newCompilationDto) {
        CompilationDto compilationDto = compilationService.addCompilation(newCompilationDto);
        return new ResponseEntity<>(compilationDto, HttpStatus.OK);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Object> deleteCompilation(@PathVariable Long compId) {
        compilationService.deleteCompilationById(compId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEventFromCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.deleteEventFromCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEventToCompilation(@PathVariable Long compId, @PathVariable Long eventId) {
        compilationService.addEventToCompilation(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{compId}/pin")
    public ResponseEntity<Object> unpinCompilation(@PathVariable Long compId) {
        compilationService.unpinCompilation(compId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{compId}/pin")
    public ResponseEntity<Object> pinCompilation(@PathVariable Long compId) {
        compilationService.pinCompilation(compId);
        return ResponseEntity.ok().build();
    }
}
