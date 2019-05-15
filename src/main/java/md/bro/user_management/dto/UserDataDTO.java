package md.bro.user_management.dto;

import lombok.Getter;
import lombok.Setter;
import md.bro.user_management.entity.Role;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@Setter
public class UserDataDTO {
    @NotNull
    @Size(max = 255)
    private String username;

    @Email
    private String email;

    @NotNull
    @Size(max = 255)
    private String password;

    private  List<Role> roles;
}
