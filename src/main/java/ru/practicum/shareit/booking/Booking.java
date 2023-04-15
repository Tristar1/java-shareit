package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Entity
@Table(name = "Bookings")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column(name = "start_Time", nullable = false)
    LocalDateTime start;
    @Column(name = "end_Time", nullable = false)
    LocalDateTime end;

    @NotNull
    @ManyToOne

    @JoinColumn(name = "item_id")
    Item item;
    @NotNull
    @ManyToOne
    @JoinColumn(name = "booker_id")
    User booker;
    @Builder.Default
    @Enumerated(EnumType.STRING)
    Status status = Status.WAITING;
}
