package ru.practicum.shareit.booking.dto;

import lombok.Data;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemBookingDto;

import java.time.LocalDateTime;

@Data
public class BookingDto {

    private int id;

    private LocalDateTime start;

    private LocalDateTime end;

    private Booker booker;

    private Status status = Status.WAITING;

    private ItemBookingDto item;
}
