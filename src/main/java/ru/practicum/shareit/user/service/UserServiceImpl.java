package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exceptions.NoSuchUserException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.utils.Messages;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User saveUser(UserDto user) {
        User myUser = userRepository.userFromDto(user);
        return userRepository.save(myUser);
    }

    @Override
    public void deleteUser(int userId) {
        userRepository.deleteById(userId);
    }

    @Override
    public User modifyUser(int userId, UserDto user) {
        User myUser = getUser(userId);
        userRepository.updateUser(user, myUser);
        return userRepository.save(myUser);
    }

    @Override
    public User getUser(int userId) {
        return userRepository.findById(userId).orElseThrow(() -> new NoSuchUserException(Messages.NO_SUCH_USER));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
