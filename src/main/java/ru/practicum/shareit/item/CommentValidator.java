package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.exception.ValidationException;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final BookingService bookingService;

    public void valid(Comment comment) throws ValidationException {

        if (comment.getText() == null || comment.getText().isBlank()){
            throw new ValidationException("Комментарий не может быть пустым!");
        }

       List<Booking> bookingList = bookingService.getBookingRepository()
                                   .findAllByBooker_IdAndStatusAndEndBefore(comment.getAuthor().getId(),
                                           Status.APPROVED, LocalDateTime.now());

       if (bookingList.isEmpty()) {
           throw new ValidationException("Вы не можете оставлять комментарйи к этой вещи так как не брали ее в аренду!");
       }
    }
}
