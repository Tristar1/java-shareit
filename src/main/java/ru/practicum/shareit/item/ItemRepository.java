package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Integer> {

    @Query("select i from Item i where i.owner.id=?1 order by i.id")
    List<Item> findAllByOwnerId(Integer userId);

     @Query("Select i from Item i where " +
            "(lower(i.description) like lower(concat('%', ?1, '%')) " +
            "or lower(i.name) like lower(concat('%', ?1, '%'))) order by id")
    List<Item> findAllByTextFilter(String textFilter, Integer userId);

}
