package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.mapper.ItemMapperDpa;
import ru.practicum.shareit.item.model.Item;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer>, ItemMapperDpa {
    List<Item> findAllByDescriptionContainsIgnoreCaseOrNameContainsIgnoreCase(@NotNull String description, @NotNull String name);
    @Query(nativeQuery = true,
            value = "select * from items i " +
            "left join responses r on r.item_id=i.item_id " +
                    "where r.request_id = ?1")
    List<Item> findAllByRequestId(int requestId);
}
