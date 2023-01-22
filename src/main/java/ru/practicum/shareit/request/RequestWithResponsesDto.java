package ru.practicum.shareit.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
public class RequestWithResponsesDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<ItemWithRequestDto> items;

}
