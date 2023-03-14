package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDao;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InMemoryItemStorage implements ItemStorage {

    private final HashMap<Integer, Item> itemList;
    private final ItemValidator itemValidator;
    private final ItemDao itemDao;
    private Integer maxId = 0;

    public InMemoryItemStorage(ItemDao itemDao) {
        this.itemList = new HashMap<>();
        this.itemValidator = new ItemValidator(this);
        this.itemDao = itemDao;
    }

    @Override
    public Item create(ItemDto itemDto) throws ValidationException {
        Item item = itemDao.itemFromData(itemDto);
        itemValidator.valid(item);
        item.setId(++maxId);
        itemList.put(item.getId(), item);
        return item;
    }

    @Override
    public Item update(ItemDto itemDto) throws ValidationException {
        Item item = itemDao.updateItemFromData(itemDto, getById(itemDto.getId()));
        itemValidator.valid(item);
        itemList.put(item.getId(), item);
        return itemList.get(item.getId());
    }

    @Override
    public Item getById(Integer id) throws ObjectNotFoundException {
        if (itemList.get(id) == null) {
            throw new ObjectNotFoundException("Предмет с ID " + id + " не существует!");
        }
        return itemList.get(id);
    }

    @Override
    public void delete(Integer id) throws ObjectNotFoundException {
        getById(id);
        itemList.remove(id);
    }

    @Override
    public List<Item> getAll(Integer ownerId) {
        return itemList.values().stream().filter(item -> Objects.equals(item.getOwner().getId(), ownerId)).collect(Collectors.toList());
    }

    @Override
    public List<Item> getByFilter(String textFilter) {
        return itemList.values().stream().filter(
                        item -> item.getAvailable() && item.getDescription().toLowerCase().contains(textFilter))
                        .collect(Collectors.toList());
    }

}
