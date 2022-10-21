package ru.yandex.practicum.ewm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.practicum.ewm.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
