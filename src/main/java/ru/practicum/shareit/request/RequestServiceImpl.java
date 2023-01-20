package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemWithRequestDto;
import ru.practicum.shareit.item.mapper.ItemRequestDtoMapper;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validators;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    @Override
    public Request addRequest(int userId, Request request) {
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkIfRequestIsNotEmpty(request);
        LocalDateTime now = LocalDateTime.now();
        request.setCreated(now);
        return requestRepository.save(request);
    }
    @Override
    public List<RequestWithResponsesDto> getMyRequests(int userId) {
        Validators.userPresenceValidator(userId, userRepository);
        return null;
    }

    @Override
    public RequestWithResponsesDto getRequest(int userId, int requestId) {
        Validators.userPresenceValidator(userId, userRepository);
        Request request = Validators.returnRequestIfPresent(requestId, requestRepository);
        List<ItemWithRequestDto> items = itemRepository.findAllByRequestId(requestId).stream()
                .map(item -> ItemRequestDtoMapper.mapToDto(item, requestId))
                .collect(Collectors.toList());
        return RequestMapper.mapToDto(request, items);
    }

    @Override
    public List<RequestWithResponsesDto> getAllRequests(int userId) {
        Validators.userPresenceValidator(userId, userRepository);
        return null;
    }
}
