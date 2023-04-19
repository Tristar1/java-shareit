package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ObjectNotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.ItemRepository;
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

    @Override
    public ItemRequest create(ItemRequestDto requestDto) throws ValidationException, ObjectNotFoundException {
        ItemRequest request = RequestMapper.mapToRequest(requestDto);
        request.setRequestor(userRepository.findById(requestDto.getRequestorId())
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + requestDto.getRequestorId())));
        RequestValidator.valid(request);
        return requestRepository.save(request);
    }

    @Override
    public ItemRequestDto getById(Integer userId, Integer id) throws ObjectNotFoundException {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userId));
        Optional<ItemRequest> request = requestRepository.findById(id);

        if (request.isEmpty()) {
            throw new ObjectNotFoundException("Запрос с id " + id + " не найден!");
        }

        return RequestMapper.mapToRequestDto(request.get());
    }

    @Override
    public List<ItemRequestDto> getAll(Integer userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + userId));
        List<ItemRequest> requestList = requestRepository.findAllByRequestorIdOrderByCreatedDesc(userId);

        return RequestMapper.mapToRequestDto(requestList);
    }

    @Override
    public List<ItemRequestDto> getAll(Integer ownerId, Integer from, Integer size) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new ObjectNotFoundException("Пользователь не найден id " + ownerId));
        List<ItemRequest> requestList = requestRepository.findAllByOwnerIdOrderByCreatedDesc(ownerId)
                .stream().skip(from).limit(size).collect(Collectors.toList());

        return RequestMapper.mapToRequestDto(requestList);
    }

}
