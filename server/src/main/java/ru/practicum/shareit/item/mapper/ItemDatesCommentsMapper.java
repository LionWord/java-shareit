package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.dto.ItemDatesCommentsDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemDatesCommentsMapper {

    public static ItemDatesCommentsDto mapFromItem(Item item, List<BookingDto> bookingDtoList) {

        ItemDatesCommentsDto newItem = new ItemDatesCommentsDto();
        newItem.setId(item.getId());
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setAvailable(item.getAvailable());
        newItem.setOwnerId(item.getOwnerId());

        if (bookingDtoList.isEmpty()) {
            newItem.setLastBooking(null);
            newItem.setNextBooking(null);
            return newItem;
        }

        boolean containsBooking = false;
        for (BookingDto b : bookingDtoList) {
            if (b.getItem().getId() == item.getId()) {
                containsBooking = true;
                break;
            }
        }
        if (containsBooking) {
            int lastBookingId = bookingDtoList.get(bookingDtoList.size() - 1).getId();
            int lastBookingBookerId = bookingDtoList.get(bookingDtoList.size() - 1).getBooker().getId();
            int nextBookingId = bookingDtoList.get(0).getId();
            int nextBookingBookerId = bookingDtoList.get(0).getBooker().getId();
            BookingShortDto lastBooking = new BookingShortDto(lastBookingId, lastBookingBookerId);
            BookingShortDto nextBooking = new BookingShortDto(nextBookingId, nextBookingBookerId);
            newItem.setLastBooking(lastBooking);
            newItem.setNextBooking(nextBooking);
            return newItem;
        }
        newItem.setLastBooking(null);
        newItem.setNextBooking(null);
        return newItem;


    }
}
