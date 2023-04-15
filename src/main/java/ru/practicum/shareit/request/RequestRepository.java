package ru.practicum.shareit.request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RequestRepository extends JpaRepository<ItemRequest, Integer> {

    List<ItemRequest> findAllByRequestorIdOrderByCreatedDesc(Integer userId);

    @Query(value = "select * from requests r inner join items i on r.id = i.request_id " +
            "where i.owner_id = ?1 order by created DESC", nativeQuery = true)
    List<ItemRequest> findAllByOwnerIdOrderByCreatedDesc(Integer userId);

}
