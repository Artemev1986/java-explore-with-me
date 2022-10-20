package ru.practicum.ewm.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.ewm.model.User;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("SELECT user FROM User user WHERE " +
            ":ids is NOT NULL AND user.id IN :ids " +
            "OR :ids is NULL ")
    List<User> getUsers(List<Long> ids, Pageable page);
}