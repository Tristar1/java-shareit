package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Primary
@Service
@RequiredArgsConstructor
public class ItemServiceImplements implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;
    private final CommentValidator commentValidator;

    @Override
    public Item create(ItemDto itemDto) throws ValidationException {
        Item item = ItemMapper.mapToNewItem(itemDto);
        ItemValidator.valid(item);
        item.setOwner(userRepository.findById(itemDto.getOwnerId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + itemDto.getOwnerId())));
        return itemRepository.save(item);
    }

    @Override
    public CommentDto createComment(CommentDto commentDto) throws ValidationException {
        Comment comment = CommentMapper.mapToNewComment(commentDto);
        comment.setAuthor(userRepository.findById(commentDto.getAuthorId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + commentDto.getAuthorId())));
        comment.setItem(itemRepository.findById(commentDto.getItemId())
                .orElseThrow(() -> new ObjectNotFoundException("Предмет не найден id " + commentDto.getItemId())));
        commentValidator.valid(comment);
        return CommentMapper.mapToCommentDto(commentRepository.save(comment));
    }

    @Override
    public Item update(ItemDto itemDto) throws ValidationException {
        Item item = itemRepository.findById(itemDto.getId()).orElseThrow();
        updateItemFields(item, itemDto);
        ItemValidator.valid(item);
        itemRepository.saveAndFlush(item);
        return itemRepository.findById(item.getId()).orElseThrow();
    }

    private void updateItemFields(Item item, ItemDto itemDto) {

        if (!Objects.equals(itemDto.getOwnerId(), item.getOwner().getId())) {
            throw new ObjectNotFoundException("Невозможно поменять владельца предмета!");
        }

        if (itemDto.getName() != null && !itemDto.getName().isBlank()) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null && !itemDto.getDescription().isBlank()) {
            item.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
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

    private void setBookings(Item item, Integer ownerId) {

        if (item.getOwner().getId().equals(ownerId)) {
            Optional<Booking> nextBooking = bookingRepository.findFirstByByItemIdAndStartAfter(item.getId(), LocalDateTime.now());
            item.setNextBooking(nextBooking.isEmpty() ? null : BookingMapper.mapToBookingDto(nextBooking.get()));

            Optional<Booking> lastBooking = bookingRepository.findFirstByItemIdAndEndBefore(item.getId(), LocalDateTime.now());
            item.setLastBooking(lastBooking.isEmpty() ? null : BookingMapper.mapToBookingDto(lastBooking.get()));
        }

    }

    private void setComments(Item item) {

        List<CommentDto> commentList = CommentMapper.mapToCommentDto(commentRepository.findAllByItem_Id(item.getId()));

        if (!commentList.isEmpty()) {
            item.setComments(commentList);
        }

    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        itemRepository.deleteById(id);
    }

    @Override
    public List<Item> getAll(Integer ownerId) {
        List<Item> itemList = new ArrayList<>();
        for (Item item : itemRepository.findAllByOwnerId(ownerId)) {
            setComments(item);
            setBookings(item, ownerId);
            itemList.add(item);
        }

        return itemList;
    }

    @Override
    public List<Item> getByFilter(String textFilter, Integer userId) {
        return itemRepository.findAllByTextFilter(textFilter, userId);
    }

}
