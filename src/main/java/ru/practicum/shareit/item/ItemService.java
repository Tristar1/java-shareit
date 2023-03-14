package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private ItemStorage itemStorage;

    public ItemService(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    public ItemStorage getItemStorage() {
        return itemStorage;
    }
}
