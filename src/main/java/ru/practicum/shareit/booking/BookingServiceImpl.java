package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public Booking create(BookingDto bookingDto) throws ValidationException {
        Booking booking = BookingMapper.toBooking(bookingDto);
        booking.setBooker(userRepository.findById(bookingDto.getBookerId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + bookingDto.getBookerId())));
        booking.setItem(itemRepository.findById(bookingDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Предмет не найден id " + bookingDto.getItemId())));
        BookingValidator.valid(booking);
        if (!bookingRepository.havingBookingsAtCurrentDate(bookingDto.getItemId(), booking.getStart()).isEmpty()) {
            throw new ValidationException("Пердмет " + booking.getItem().toString() +
                    " уже забронирован в интервале с " + bookingDto.getStart() + " по " + bookingDto.getEnd() + " !");
        }
        return bookingRepository.save(booking);
    }

    @Override
    public Booking update(BookingDto bookingDto) throws ValidationException {
        Booking booking = bookingRepository.findByIdAndOwner(bookingDto.getId(), bookingDto.getBookerId());
        if (booking == null) {
            throw new ObjectNotFoundException("Бронь с id " + bookingDto.getId() + " не найдена!");
        }
        if (booking.getStatus() == Status.APPROVED) {
            throw new ValidationException("Нельзя менять статус после утверждения!");
        }
        BookingMapper.updateBookingFields(booking, bookingDto);
        return bookingRepository.save(booking);
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

        Booking booking = (bookingRepository.findByIdAndAndBooker_Id(id, userId) == null

                ? bookingRepository.findByIdAndOwner(id, userId) : bookingRepository.findByIdAndAndBooker_Id(id, userId));
        if (booking == null) {
            throw new ObjectNotFoundException("Бронь с id " + id + " не найдена!");
        }
        return booking;
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<Booking> getAllByOwner(Integer ownerId, String state,
                                       Integer from, Integer size, LocalDateTime dateTime) throws ObjectNotFoundException, ValidationException {
        userRepository.findById(ownerId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + ownerId));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case "ALL":
                return bookingRepository.findAllByItemOwner(ownerId).stream().skip(from).limit(size).collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByItemOwnerAndStatus(ownerId, Status.WAITING.toString().toUpperCase())
                        .stream().skip(from).limit(size).collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByItemOwnerAndStatus(ownerId, Status.REJECTED.toString().toUpperCase())
                        .stream().skip(from).limit(size).collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByItemOwnerAndEndBefore(ownerId, dateTime)
                        .stream().skip(from).limit(size).collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByItemOwnerAndStartAfter(ownerId, dateTime)
                        .stream().skip(from).limit(size).collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findCurrentOwnerBookings(ownerId, dateTime)
                        .stream().skip(from).limit(size).collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> getAll(Integer bookerId, String state,
                                Integer from, Integer size, LocalDateTime dateTime) throws ValidationException {
        userRepository.findById(bookerId).orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + bookerId));
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (state) {
            case "ALL":
                return bookingRepository.findAllByBooker_Id(bookerId, sort).stream().skip(from).limit(size).collect(Collectors.toList());
            case "WAITING":
                return bookingRepository.findAllByBooker_IdAndStatus(bookerId,
                        Status.WAITING, sort).stream().skip(from).limit(size).collect(Collectors.toList());
            case "REJECTED":
                return bookingRepository.findAllByBooker_IdAndStatus(bookerId,
                        Status.REJECTED, sort).stream().skip(from).limit(size).collect(Collectors.toList());
            case "PAST":
                return bookingRepository.findAllByEndBefore(dateTime, sort).stream().skip(from).limit(size).collect(Collectors.toList());
            case "FUTURE":
                return bookingRepository.findAllByStartAfter(dateTime, sort).stream().skip(from).limit(size).collect(Collectors.toList());
            case "CURRENT":
                return bookingRepository.findCurrentBookings(bookerId, dateTime).stream().skip(from).limit(size).collect(Collectors.toList());
            default:
                throw new ValidationException("Unknown state: " + state);
        }

    }

}
