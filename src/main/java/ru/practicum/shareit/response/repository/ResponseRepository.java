package ru.practicum.shareit.response.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.response.model.Response;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Integer> {
}
