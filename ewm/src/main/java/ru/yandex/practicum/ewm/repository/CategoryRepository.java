package ru.yandex.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewm.model.Category;

import javax.persistence.EntityNotFoundException;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    default Category getById(Long id) {
        return findById(id).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id (" + id + ") not found"))
        );
    }
}
