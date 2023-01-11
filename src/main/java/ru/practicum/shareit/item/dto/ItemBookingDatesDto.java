package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;

@Getter
@Setter
@NoArgsConstructor
public class ItemBookingDatesDto extends Item {

    private BookingShortDto lastBooking;

    private BookingShortDto nextBooking;

}
