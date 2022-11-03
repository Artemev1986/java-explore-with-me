package ru.yandex.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.practicum.ewm.model.Compilation;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation, Long> {

    @Query("SELECT comp FROM Compilation comp WHERE :pinned IS NOT NULL AND comp.pinned = :pinned  OR :pinned IS NULL")
    List<Compilation> findAllByPinned(Boolean pinned, Pageable page);

    default Compilation getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("The event with id (" + id + ") not found"))
        );
    }
}
