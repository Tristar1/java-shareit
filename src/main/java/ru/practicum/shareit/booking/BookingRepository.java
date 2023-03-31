package ru.practicum.shareit.booking;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public interface BookingRepository extends JpaRepository<Booking, Integer> {

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where b.id = ?1 and (i.user_id = ?2 or b.user_id = ?2) \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findByIdAndBookerOrOwner(Integer id, Integer userId);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where b.id = ?1 and i.user_id = ?2 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findByIdAndOwner(Integer id, Integer userId);

    List<Booking> findAllByBooker_Id(Integer bookerId, Sort sort);

    List<Booking> findAllByBooker_IdAndStatus(Integer bookerId, Status status, Sort sort);

    List<Booking> findAllByEndBefore(LocalDateTime localDateTime, Sort sort);

    List<Booking> findAllByStartAfter(LocalDateTime localDateTime, Sort sort);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.user_id = ?1 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwner(Integer ownerId);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.user_id = ?1 and b.status like ?2 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndStatus(Integer ownerId, String status);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.user_id = ?1 and b.end_time < ?2 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndEndBefore(Integer ownerId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.user_id = ?1 and b.start_time > ?2 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findAllByItemOwnerAndStartAfter(Integer ownerId, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b\n" +
            "left join items as i on b.item_id = i.id  \n" +
            "where i.user_id = ?1 and b.start_time <= ?2 and b.end_time > ?2 \n" +
            "order by b.start_time DESC", nativeQuery = true)
    List<Booking> findCurrentOwnerBookings(Integer userId, LocalDateTime localDateTime);

    @Query("select b from Booking b where b.item.id = ?1 and b.start >= ?2 and b.end <= ?2 and b.status = 'APPROVED'")
    List<Booking> havingBookingsAtCurrentDate(Integer itemId, LocalDateTime start);

    @Query(value = "select * from bookings as b where item_id = ?1 and start_time > ?2 " +
            "and status like 'APPROVED'order by start_time limit 1",
            nativeQuery = true)
    Optional<Booking> findFirstByByItemIdAndStartAfter(Integer itemId, LocalDateTime startTime);

    @Query(value = "select * from bookings as b where item_id = ?1 and (end_time < ?2 or start_time < ?2)" +
            "and status like 'APPROVED' order by end_time DESC limit 1",
            nativeQuery = true)
    Optional<Booking> findFirstByItemIdAndEndBefore(Integer itemId, LocalDateTime endTime);

    List<Booking> findAllByBooker_IdAndStatusAndEndBefore(Integer userId, Status status, LocalDateTime localDateTime);

    @Query(value = "select * from bookings as b where user_id = ?1 and start_time <= ?2  " +
            "and end_time > ?2 order by end_time DESC",
            nativeQuery = true)
    List<Booking> findCurrentBookings(Integer userId, LocalDateTime localDateTime);

}
