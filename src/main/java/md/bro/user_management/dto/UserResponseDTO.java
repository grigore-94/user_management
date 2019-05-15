package md.bro.user_management.dto;

import lombok.Getter;
import lombok.Setter;
import md.bro.user_management.entity.Role;

import java.util.List;

@Getter
@Setter
public class UserResponseDTO {
  private Integer id;
  private String username;
  private String email;
  private int rank;
  private List<Role> roles;
}
