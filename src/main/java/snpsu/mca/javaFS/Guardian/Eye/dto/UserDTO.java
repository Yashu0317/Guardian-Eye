package snpsu.mca.javaFS.Guardian.Eye.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String password;
    private LocalDate dateOfBirth;
    private String address;
    private String bloodGroup;
}