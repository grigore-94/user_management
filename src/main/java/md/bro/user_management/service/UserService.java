package md.bro.user_management.service;

import lombok.RequiredArgsConstructor;
import md.bro.user_management.converter.UserConverter;
import md.bro.user_management.dto.UserResponseDTO;
import md.bro.user_management.entity.User;
import md.bro.user_management.exception.CustomException;
import md.bro.user_management.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserConverter converter;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserResponseDTO signup(User user) {
        if (!repository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);

            return converter.convertToUserResponseDto(user);
        } else {
            throw new CustomException("Username is already in use", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public void delete(String username) {
        repository.deleteByUsername(username);
    }

    public UserResponseDTO search(String username) {
        User user = repository.findByUsername(username);
        if (user == null) {
            throw new CustomException("The user doesn't exist", HttpStatus.NOT_FOUND);
        }
        return converter.convertToUserResponseDto(user);
    }

    public List<UserResponseDTO> retrieve(Pageable pageable) {

        return repository
                .findAll(pageable)
                .stream()
                .map(converter::convertToUserResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> get(long id) {
        Optional<User> user = repository.findById(id);
        return Optional.ofNullable(user.isPresent() ? converter.convertToUserResponseDto(user.get()) : null);
    }
}
