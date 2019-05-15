package md.bro.user_management.service;

import lombok.RequiredArgsConstructor;
import md.bro.user_management.converter.UserConverter;
import md.bro.user_management.dto.UserResponseDTO;
import md.bro.user_management.entity.User;
import md.bro.user_management.exception.CustomException;
import md.bro.user_management.repository.UserRepository;
import md.bro.user_management.security.JwtTokenProvider;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserConverter converter;
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    public String signin(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

            return jwtTokenProvider.createToken(username, repository.findByUsername(username).getRoles());
        } catch (AuthenticationException e) {
            throw new CustomException("Invalid username/password supplied", HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }

    public String signup(User user) {
        if (!repository.existsByUsername(user.getUsername())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            repository.save(user);
            return jwtTokenProvider.createToken(user.getUsername(), user.getRoles());
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

    public UserResponseDTO whoami(HttpServletRequest req) {
        return converter
                .convertToUserResponseDto(
                        repository.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(req)))
                );
    }

    public String refresh(String username) {
        return jwtTokenProvider.createToken(username, repository.findByUsername(username).getRoles());
    }

    public List<UserResponseDTO> retrieve(Pageable pageable) {

        return repository
                .findAll(pageable)
                .stream()
                .map(converter::convertToUserResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<UserResponseDTO> get(long id) {
        return Optional.ofNullable(converter.convertToUserResponseDto(repository.findById(id).orElse(null)));
    }
}
