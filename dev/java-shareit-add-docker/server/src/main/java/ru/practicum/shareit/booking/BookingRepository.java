package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Booking findByIdAndAndBooker_Id(Long id, Long userId);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where b.id = ?1 and i.owner_id = ?2", nativeQuery = true)
    Booking findByIdAndOwner(Long id, Long userId);

    List<Booking> findAllByBooker_Id(Long bookerId, Sort sort);

    List<Booking> findAllByBooker_IdAndStatus(Long bookerId, Status status, Sort sort);

    List<Booking> findAllByEndBefore(LocalDateTime localDateTime, Sort sort);

    List<Booking> findAllByStartAfter(LocalDateTime localDateTime, Sort sort);

    @Query(value = "select * from bookings as b \n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.owner_id = ?1 order by start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwner(Long ownerId);

    @Query(value = "select * from bookings as b \n" +
            "left join items as i on b.item_id = i.id \n" +
            "where i.owner_id = ?1 and b.status like ?2 order by start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndStatus(Long ownerId, String status);

    @Query(value = "select * from bookings as b \n" +
            "left join items as i on b.item_id = i.id \n" +
            "where i.owner_id = ?1 and b.end_time < ?2 order by start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndEndBefore(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b \n" +
            "left join items as i on b.item_id = i.id \n" +
            "where i.owner_id = ?1 and b.start_time > ?2 order by start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndStartAfter(Long ownerId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b \n" +
            "left join items as i on b.item_id = i.id \n" +
            "where i.owner_id = ?1 and b.start_time <= ?2 and b.end_time > ?2 order by start_time DESC", nativeQuery = true)
    List<Booking> findCurrentOwnerBookings(Long userId, LocalDateTime localDateTime);

    @Query("select b from Booking b where b.item.id = ?1 and b.start <= ?2 and b.end >= ?2 and b.status = 'APPROVED'")

    List<Booking> havingBookingsAtCurrentDate(Long itemId, LocalDateTime start);

    @Query(value = "select * from bookings as b where item_id = ?1 and start_time > ?2 " +
            "and status like 'APPROVED'order by start_time limit 1",
            nativeQuery = true)
    Optional<Booking> findFirstByByItemIdAndStartAfter(Long itemId, LocalDateTime startTime);

    @Query(value = "select * from bookings as b where item_id = ?1 and (end_time < ?2 or start_time < ?2)" +
            "and status like 'APPROVED' order by end_time DESC limit 1", nativeQuery = true)
    Optional<Booking> findFirstByItemIdAndEndBefore(Long itemId, LocalDateTime endTime);

    List<Booking> findAllByBooker_IdAndStatusAndEndBefore(Long userId, Status status, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b where b.booker_id = ?1 and b.start_time <= ?2  " +
            "and b.end_time > ?2 order by start_time DESC", nativeQuery = true)
    List<Booking> findCurrentBookings(Long userId, LocalDateTime localDateTime);

}
