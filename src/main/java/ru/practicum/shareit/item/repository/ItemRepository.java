package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.mapper.ItemMapperDpa;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>, ItemMapperDpa {
    Page<Item> findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(@NotNull String description,
                                                                              @NotNull String name,
                                                                              Pageable pageable);

    @Query("select i from Item i " +
                    "join Response r on r.itemId = i.id " +
                    "where r.requestId = ?1")
    List<Item> findAllByRequestId(int requestId);

    Page<Item> findAllByOwnerIdOrderByIdAsc(int userId, Pageable pageable);
}
