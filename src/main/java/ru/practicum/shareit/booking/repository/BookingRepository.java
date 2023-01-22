package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.booking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAllByBookerIdOrderByStartDesc(int bookerId, Pageable pageable);

    @Query ("select b from Booking b " +
            "join Item i on i.id=b.itemId " +
            "where i.ownerId=?1 " +
            "order by b.start desc ")
    Page<Booking> findAllOwnerBookings(int ownerId, Pageable pageable);
}
