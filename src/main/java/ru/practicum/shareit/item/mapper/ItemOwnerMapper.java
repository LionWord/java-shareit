package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.mapper.BookingMapperDpa;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemForOwnerDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public class ItemOwnerMapper {

    public static ItemForOwnerDto mapFromItem(Item item, List<BookingDto> bookingDtoList) {

        ItemForOwnerDto newItem = new ItemForOwnerDto();
        newItem.setId(item.getId());
        newItem.setName(item.getName());
        newItem.setDescription(item.getDescription());
        newItem.setAvailable(item.getAvailable());

        if (bookingDtoList.isEmpty()) {
            newItem.setLastBooking(null);
            newItem.setNextBooking(null);
            return newItem;
        }

        Integer lastBookingId = bookingDtoList.get(bookingDtoList.size() - 1).getId();
        Integer lastBookingBookerId = bookingDtoList.get(bookingDtoList.size() - 1).getBooker().getId();
        Integer nextBookingId = bookingDtoList.get(0).getId();
        Integer nextBookingBookerId = bookingDtoList.get(0).getBooker().getId();
        BookingShortDto lastBooking = new BookingShortDto(lastBookingId, lastBookingBookerId);
        BookingShortDto nextBooking = new BookingShortDto(nextBookingId, nextBookingBookerId);
        newItem.setLastBooking(lastBooking);
        newItem.setNextBooking(nextBooking);
        return newItem;



    }
}
