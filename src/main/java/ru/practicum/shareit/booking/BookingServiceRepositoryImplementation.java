package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookingServiceRepositoryImplementation implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking create(BookingDto bookingDto) throws ValidationException {
        Booking booking = BookingMapper.mapToNewBooking(bookingDto);
        booking.setBooker(userRepository.findById(bookingDto.getBookerId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + bookingDto.getBookerId())));
        booking.setItem(itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Предмет не найден id " + bookingDto.getItemId())));
        BookingValidator.valid(booking);
        if (!bookingRepository.havingBookingsAtCurrentDate(bookingDto.getItemId(), booking.getStart()).isEmpty()) {
            throw new ValidationException("Этот пердмет уже забронирован на эти даты!");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(BookingDto bookingDto) throws ValidationException {
        Booking booking = getFirstByBookingList(bookingRepository.findByIdAndOwner(bookingDto.getId(), bookingDto.getBookerId()));
        if (booking.getStatus() == Status.APPROVED) {
            throw new ValidationException("Нельзя менять статус после утверждения!");
        }
        updateBookingFields(booking, bookingDto);
        //BookingValidator.valid(booking);
        return bookingRepository.save(booking);
    }

    private void updateBookingFields(Booking booking, BookingDto bookingDto) {

        if (bookingDto.getStart() != null) {
            booking.setStart(bookingDto.getStart());
        }
        if (bookingDto.getEnd() != null) {
            booking.setEnd(bookingDto.getEnd());
        }
        if (bookingDto.getStatus() != null) {
            booking.setStatus(Status.valueOf(bookingDto.getStatus()));
        }

    }

    @Override
    public Booking getById(Integer id) throws ObjectNotFoundException {
        Optional<Booking> booking = bookingRepository.findById(id);

        if (booking.isEmpty()) {
            throw new ObjectNotFoundException("Бронь с id " + id + " не найдена!");
        }

        return booking.get();
    }

    @Override
    public Booking getByIdAndUserId(Integer id, Integer userId) throws ObjectNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userId));

        return getFirstByBookingList(bookingRepository.findByIdAndBookerOrOwner(id, userId));
    }

    private Booking getFirstByBookingList(List<Booking> bookingList) {
        if (bookingList.isEmpty()) {
            throw new ObjectNotFoundException("Бронь не найдена!");
        }

        return bookingList.get(0);
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllByOwner(Integer ownerId, String state) throws ObjectNotFoundException, ValidationException {
        userRepository.findById(ownerId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + ownerId));
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwner(ownerId);
            case "WAITING":
                return bookingRepository.findAllByItemOwnerAndStatus(ownerId, Status.WAITING.toString().toUpperCase());
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerAndStatus(ownerId, Status.REJECTED.toString().toUpperCase());
            case "PAST":
                return bookingRepository.findAllByItemOwnerAndEndBefore(ownerId, LocalDateTime.now());
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerAndStartAfter(ownerId, LocalDateTime.now());
            case "CURRENT":
                return bookingRepository.findCurrentOwnerBookings(ownerId, LocalDateTime.now());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    @Override
    public BookingRepository getBookingRepository() {
        return bookingRepository;
    }

    @Override
    public List<Booking> getAll(Integer bookerId, String state) throws ValidationException {
        userRepository.findById(bookerId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + bookerId));

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_Id(bookerId, Sort.by(Sort.Direction.DESC, "start"));
            case "WAITING":
                return bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.WAITING, Sort.by(Sort.Direction.DESC, "start"));
            case "REJECTED":
                return bookingRepository.findAllByBooker_IdAndStatus(bookerId, Status.REJECTED, Sort.by(Sort.Direction.DESC, "start"));
            case "PAST":
                return bookingRepository.findAllByEndBefore(LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
            case "FUTURE":
                return bookingRepository.findAllByStartAfter(LocalDateTime.now(), Sort.by(Sort.Direction.DESC, "start"));
            case "CURRENT":
                return bookingRepository.findCurrentBookings(bookerId, LocalDateTime.now());
            default:
                throw new ValidationException("Unknown state: " + state);
        }

    }

}
