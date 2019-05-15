package md.bro.user_management.converter;

import lombok.RequiredArgsConstructor;
import md.bro.user_management.dto.UserResponseDTO;
import md.bro.user_management.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserConverter {
    private final ModelMapper modelMapper;

    public UserResponseDTO convertToUserResponseDto(User user) {
        return modelMapper.map(user, UserResponseDTO.class);
    }
}