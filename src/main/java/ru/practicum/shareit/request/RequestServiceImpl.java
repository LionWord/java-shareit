package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Validators;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class RequestServiceImpl implements RequestService{

    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    @Override
    public Request addRequest(int userId, Request request) {
        Validators.userPresenceValidator(userId, userRepository);
        Validators.checkIfRequestIsNotEmpty(request);
        LocalDateTime now = LocalDateTime.now();
        request.setCreated(now);
        return requestRepository.save(request);
    }
    @Override
    public List<RequestDto> getMyRequests(int userId) {
        Validators.userPresenceValidator(userId, userRepository);
        return null;
    }

    @Override
    public RequestDto getRequest(int userId, int requestId) {
        Validators.userPresenceValidator(userId, userRepository);
        return null;
    }

    @Override
    public List<RequestDto> getAllRequests(int userId) {
        Validators.userPresenceValidator(userId, userRepository);
        return null;
    }
}
