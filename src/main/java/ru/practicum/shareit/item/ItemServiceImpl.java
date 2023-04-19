package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    public ItemDto create(ItemDto itemDto) throws ValidationException {
        Item item = ItemMapper.mapToItem(itemDto);
        ItemValidator.valid(item);
        item.setOwner(userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + itemDto.getOwnerId())));
        if (itemDto.getRequestId() != null) {
            item.setRequest(requestRepository.findById(itemDto.getRequestId()).orElseThrow());
        }
        return ItemMapper.mapToItemDto(itemRepository.save(item));
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) throws ValidationException {
        Comment comment = CommentMapper.mapToNewComment(commentDto);
        comment.setAuthor(userRepository.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + commentDto.getAuthorId())));
        comment.setItem(itemRepository.findById(commentDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Предмет не найден id " + commentDto.getItemId())));

        if (bookingRepository.findAllByBooker_IdAndStatusAndEndBefore(comment.getAuthor().getId(),
                Status.APPROVED, comment.getCreated()).isEmpty()) {
            throw new ValidationException("Вы не можете оставлять комментарйи к этой вещи так как не брали ее в аренду!");
        }
        CommentValidator.valid(comment);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public ItemDto update(ItemDto itemDto) throws ValidationException {
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow();
        ItemMapper.updateItemFields(item, itemDto);
        ItemValidator.valid(item);
        itemRepository.save(item);
        return ItemMapper.mapToItemDto(itemRepository.findById(item.getId()).orElseThrow());
    }

    @Override
    public Item getById(Integer id) throws ObjectNotFoundException {
        Optional<Item> item = itemRepository.findById(id);

        if (item.isEmpty()) {
            throw new ObjectNotFoundException("Предмет с id " + id + " не найден!");
        }

        return item.get();
    }

    @Override
    public Item getById(Integer id, Integer ownerId) throws ObjectNotFoundException {
        Optional<Item> item = itemRepository.findById(id);

        if (item.isEmpty()) {
            throw new ObjectNotFoundException("Предмет с id " + id + " не найден!");
        }

        setBookings(item.get(), ownerId);
        setComments(item.get());

        return item.get();
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> getAll(Integer ownerId, Integer from, Integer size) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemRepository.findAllByOwnerId(ownerId).stream().skip(from).limit(size).collect(Collectors.toList())) {
            setComments(item);
            setBookings(item, ownerId);
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public List<Item> getByFilter(String textFilter, Integer userId, Integer from, Integer size) {
        return itemRepository.findAllByTextFilter(textFilter, userId).stream().skip(from).limit(size).collect(Collectors.toList());
    }

    private void setBookings(Item item, Integer ownerId) {

        if (item.getOwner().getId().equals(ownerId)) {
            Optional<Booking> nextBooking = bookingRepository.findFirstByByItemIdAndStartAfter(item.getId(), LocalDateTime.now());
            item.setNextBooking(nextBooking.isEmpty() ? null : BookingMapper.toBookingDto(nextBooking.get()));

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndEndBefore(item.getId(), LocalDateTime.now());
            item.setLastBooking(lastBooking.isEmpty() ? null : BookingMapper.toBookingDto(lastBooking.get()));
        }
    }

    private void setComments(Item item) {

        List<CommentDto> commentList = CommentMapper.mapToCommentDto(commentRepository.findAllByItem_Id(item.getId()));

        if (!commentList.isEmpty()) {
            item.setComments(commentList);
        }
    }

}
