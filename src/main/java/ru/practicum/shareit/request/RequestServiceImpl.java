package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.RequestMapper;
import ru.practicum.shareit.user.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequest create(ItemRequestDto requestDto) throws ValidationException, ObjectNotFoundException {
        ItemRequest request = RequestMapper.mapToRequest(requestDto);
        request.setRequestor(userRepository.findById(requestDto.getRequestorId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + requestDto.getRequestorId())));
        RequestValidator.valid(request);
        return requestRepository.save(request);
    }

    @Override
    public ItemRequest getById(Integer userId, Integer id) throws ObjectNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userId));
        Optional<ItemRequest> request = requestRepository.findById(id);

        if (request.isEmpty()) {
            throw new ObjectNotFoundException("Запрос с id " + id + " не найден!");
        }

        setItems(request.get());
        return request.get();
    }

    @Override
    public List<ItemRequest> getAll(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userId));
        List<ItemRequest> requestList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);

        for (ItemRequest request : requestList) {
            setItems(request);
        }

        return requestList;
    }

    @Override
    public List<ItemRequest> getAll(Integer ownerId, Integer from, Integer size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + ownerId));
        List<ItemRequest> requestList = requestRepository.findAllByOwnerIdOrderByCreatedDesc(ownerId)
                .stream().skip(from).limit(size).collect(Collectors.toList());
        for (ItemRequest request : requestList) {
            setItems(request);
        }

        return requestList;
    }

    private void setItems(ItemRequest itemRequest) {
        List<Item> itemList = itemRepository.findAllByRequest_Id(itemRequest.getId());
        itemRequest.setItems(ItemMapper.mapToItemDto(itemList));
    }
}
