package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestRepository extends JpaRepository<Request, Integer> {
    List<Request> findAllByRequesterIdOrderByCreatedDesc(int requesterId);

    @Query("select r from Request r " +
            "where r.requesterId not in ?1 " +
            "order by r.created desc")
    Page<Request> findAllOrderByCreated(int userId, Pageable pageable);

}
