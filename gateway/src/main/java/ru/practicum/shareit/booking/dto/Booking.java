package ru.practicum.shareit.booking.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Booking {

    private int id;

    private LocalDateTime start;

    private LocalDateTime end;

    private int itemId;

    private int bookerId;

    private Status status = Status.WAITING;

}
