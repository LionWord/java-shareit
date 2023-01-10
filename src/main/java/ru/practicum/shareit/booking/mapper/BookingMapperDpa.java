package ru.practicum.shareit.booking.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.booking.model.Booker;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemBookingDto;
import ru.practicum.shareit.item.repository.ItemRepository;

@Component
public class BookingMapperDpa {
    public static BookingDto make(Booking booking, ItemRepository itemRepository) {
        BookingDto bookingDto = new BookingDto();
        int itemId = booking.getItemId();
        ItemBookingDto item = new ItemBookingDto(itemId, itemRepository.findById(itemId).get().getName());
        Booker booker = new Booker(booking.getBookerId());
        bookingDto.setId(booking.getId());
        bookingDto.setStatus(booking.getStatus());
        bookingDto.setStart(booking.getStart());
        bookingDto.setEnd(booking.getEnd());
        bookingDto.setBooker(booker);
        bookingDto.setItem(item);
        return bookingDto;
    };
    public static BookingShortDto makeShort(int bookingId, int bookerId) {
        BookingShortDto shortDto = new BookingShortDto();
        shortDto.setId(bookingId);
        shortDto.setBookerId(bookerId);
        return shortDto;
    }
}
