package snpsu.mca.javaFS.Guardian.Eye.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserFamilyDTO {
    private Long id;
    private Long userId;
    private String name;
    private String relationship;
    private String phone;
    private String email;
    private String address;
    private Boolean isEmergencyContact;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}