package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;
import ru.practicum.shareit.item.dto.CommentRequestDto;
import ru.practicum.shareit.item.dto.ItemDto;

import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ItemClient extends BaseClient {

    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> getItems(Long ownerId, Integer from, Integer size, LocalDateTime dateTime) {
        Map<String, Object> parameters = Map.of(
                "from", from,
                "size", size,
                "dateTime", dateTime);
        return get("" + "?from={from}&size={size}&dateTime={dateTime}", ownerId, parameters);
    }

    public ResponseEntity<Object> createItem(ItemDto requestDto) {
        return post("", requestDto);
    }

    public ResponseEntity<Object> createComment(CommentRequestDto requestDto) {
        return post("/" + requestDto.getItemId() + "/comment", requestDto);
    }

    public ResponseEntity<Object> getItem(Long itemId, LocalDateTime dateTime) {
        Map<String, Object> parameters = Map.of(
                "dateTime", dateTime);
        return get("/" + itemId + "?dateTime={dateTime}", null, parameters);
    }

    public ResponseEntity<Object> getItem(Long itemId, Long ownerId, LocalDateTime dateTime) {
        Map<String, Object> parameters = Map.of(
                "dateTime", dateTime);
        return get("/" + itemId + "?dateTime={dateTime}", ownerId, parameters);
    }

    public ResponseEntity<Object> updateItem(ItemDto requestDto) {
        return patch("/" + requestDto.getId(), requestDto);
    }

    public ResponseEntity<Object> deleteItem(Long itemId) {
        return delete("/" + itemId, itemId);
    }

    public ResponseEntity<Object> getItemsByFilter(String text, Long ownerId, Integer from, Integer size) {
        Map<String, Object> parameters = Map.of(
                "text", text,
                "from", from,
                "size", size);
        return get("/search?text={text}&from={from}&size={size}", ownerId, parameters);
    }

}
