package snpsu.mca.javaFS.Guardian.Eye.service;

import snpsu.mca.javaFS.Guardian.Eye.entity.User;
import snpsu.mca.javaFS.Guardian.Eye.entity.UserFamily;
import snpsu.mca.javaFS.Guardian.Eye.repository.UserFamilyRepository;
import snpsu.mca.javaFS.Guardian.Eye.repository.UserRepository;
import snpsu.mca.javaFS.Guardian.Eye.dto.UserFamilyDTO;
import snpsu.mca.javaFS.Guardian.Eye.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserFamilyService {
    
    private final UserFamilyRepository userFamilyRepository;
    private final UserRepository userRepository;
    
    @Autowired
    public UserFamilyService(UserFamilyRepository userFamilyRepository, UserRepository userRepository) {
        this.userFamilyRepository = userFamilyRepository;
        this.userRepository = userRepository;
    }
    
    public ResponseDTO createUserFamily(UserFamilyDTO userFamilyDTO) {
        try {
            Optional<User> user = userRepository.findById(userFamilyDTO.getUserId());
            if (!user.isPresent()) {
                return ResponseDTO.error("User not found");
            }
            
            UserFamily userFamily = new UserFamily();
            userFamily.setUser(user.get());
            userFamily.setName(userFamilyDTO.getName());
            userFamily.setRelationship(userFamilyDTO.getRelationship());
            userFamily.setPhone(userFamilyDTO.getPhone());
            userFamily.setEmail(userFamilyDTO.getEmail());
            userFamily.setAddress(userFamilyDTO.getAddress());
            userFamily.setIsEmergencyContact(userFamilyDTO.getIsEmergencyContact());
            
            UserFamily savedUserFamily = userFamilyRepository.save(userFamily);
            return ResponseDTO.success("Family member added successfully", convertToDTO(savedUserFamily));
            
        } catch (Exception e) {
            return ResponseDTO.error("Error adding family member: " + e.getMessage());
        }
    }
    
    public ResponseDTO getUserFamiliesByUserId(Long userId) {
        try {
            List<UserFamily> userFamilies = userFamilyRepository.findByUserId(userId);
            List<UserFamilyDTO> userFamilyDTOs = userFamilies.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.success("Family members retrieved successfully", userFamilyDTOs);
        } catch (Exception e) {
            return ResponseDTO.error("Error retrieving family members: " + e.getMessage());
        }
    }
    
    public ResponseDTO getUserFamilyById(Long id) {
        try {
            Optional<UserFamily> userFamily = userFamilyRepository.findById(id);
            if (userFamily.isPresent()) {
                return ResponseDTO.success("Family member retrieved successfully", convertToDTO(userFamily.get()));
            } else {
                return ResponseDTO.error("Family member not found");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error retrieving family member: " + e.getMessage());
        }
    }
    
    public ResponseDTO updateUserFamily(Long id, UserFamilyDTO userFamilyDTO) {
        try {
            Optional<UserFamily> existingUserFamily = userFamilyRepository.findById(id);
            if (existingUserFamily.isPresent()) {
                UserFamily userFamily = existingUserFamily.get();
                
                userFamily.setName(userFamilyDTO.getName());
                userFamily.setRelationship(userFamilyDTO.getRelationship());
                userFamily.setPhone(userFamilyDTO.getPhone());
                userFamily.setEmail(userFamilyDTO.getEmail());
                userFamily.setAddress(userFamilyDTO.getAddress());
                userFamily.setIsEmergencyContact(userFamilyDTO.getIsEmergencyContact());
                
                UserFamily updatedUserFamily = userFamilyRepository.save(userFamily);
                return ResponseDTO.success("Family member updated successfully", convertToDTO(updatedUserFamily));
            } else {
                return ResponseDTO.error("Family member not found");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error updating family member: " + e.getMessage());
        }
    }
    
    public ResponseDTO deleteUserFamily(Long id) {
        try {
            Optional<UserFamily> userFamily = userFamilyRepository.findById(id);
            if (userFamily.isPresent()) {
                userFamilyRepository.delete(userFamily.get());
                return ResponseDTO.success("Family member deleted successfully");
            } else {
                return ResponseDTO.error("Family member not found");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error deleting family member: " + e.getMessage());
        }
    }
    
    private UserFamilyDTO convertToDTO(UserFamily userFamily) {
        UserFamilyDTO dto = new UserFamilyDTO();
        dto.setId(userFamily.getId());
        dto.setUserId(userFamily.getUser().getId());
        dto.setName(userFamily.getName());
        dto.setRelationship(userFamily.getRelationship());
        dto.setPhone(userFamily.getPhone());
        dto.setEmail(userFamily.getEmail());
        dto.setAddress(userFamily.getAddress());
        dto.setIsEmergencyContact(userFamily.getIsEmergencyContact());
        return dto;
    }
}