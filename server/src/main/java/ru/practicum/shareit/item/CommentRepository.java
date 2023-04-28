package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query(value =  "select * from Comments where item_id = ?1 order by id", nativeQuery = true)
    List<Comment> findAllByItem_Id(Long itemId);

}
