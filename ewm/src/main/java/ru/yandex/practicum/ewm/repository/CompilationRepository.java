package ru.yandex.practicum.ewm.repository;

import io.micrometer.core.lang.Nullable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ewm.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT comp FROM Compilation comp WHERE :pinned IS NOT NULL AND comp.pinned = :pinned  OR :pinned IS NULL")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);
}
