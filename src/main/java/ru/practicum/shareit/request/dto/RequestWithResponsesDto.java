package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder(access = AccessLevel.PUBLIC)
@NoArgsConstructor
@AllArgsConstructor
public class RequestWithResponsesDto {
    private Integer id;
    private String description;
    private LocalDateTime created;
    private List<ItemWithRequestDto> items;

}
