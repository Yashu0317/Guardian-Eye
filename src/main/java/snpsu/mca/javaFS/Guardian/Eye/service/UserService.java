package snpsu.mca.javaFS.Guardian.Eye.service;

import snpsu.mca.javaFS.Guardian.Eye.entity.User;
import snpsu.mca.javaFS.Guardian.Eye.repository.UserRepository;
import snpsu.mca.javaFS.Guardian.Eye.dto.UserDTO;
import snpsu.mca.javaFS.Guardian.Eye.dto.ResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public ResponseDTO createUser(UserDTO userDTO) {
        try {
            if (userRepository.existsByEmail(userDTO.getEmail())) {
                return ResponseDTO.error("Email already registered");
            }
            
            if (userRepository.existsByPhone(userDTO.getPhone())) {
                return ResponseDTO.error("Phone number already registered");
            }
            
            User user = new User();
            user.setName(userDTO.getName());
            user.setEmail(userDTO.getEmail());
            user.setPhone(userDTO.getPhone());
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            user.setDateOfBirth(userDTO.getDateOfBirth());
            user.setAddress(userDTO.getAddress());
            user.setBloodGroup(userDTO.getBloodGroup());
            
            User savedUser = userRepository.save(user);
            return ResponseDTO.success("User created successfully", convertToDTO(savedUser));
            
        } catch (Exception e) {
            return ResponseDTO.error("Error creating user: " + e.getMessage());
        }
    }
    
    public ResponseDTO getAllUsers() {
        try {
            List<User> users = userRepository.findByIsActiveTrue();
            List<UserDTO> userDTOs = users.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.success("Users retrieved successfully", userDTOs);
        } catch (Exception e) {
            return ResponseDTO.error("Error retrieving users: " + e.getMessage());
        }
    }
    
    public ResponseDTO getUserById(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent() && user.get().getIsActive()) {
                return ResponseDTO.success("User retrieved successfully", convertToDTO(user.get()));
            } else {
                return ResponseDTO.error("User not found or inactive");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error retrieving user: " + e.getMessage());
        }
    }
    
    public ResponseDTO updateUser(Long id, UserDTO userDTO) {
        try {
            Optional<User> existingUser = userRepository.findById(id);
            if (existingUser.isPresent() && existingUser.get().getIsActive()) {
                User user = existingUser.get();
                
                if (!user.getEmail().equals(userDTO.getEmail()) && 
                    userRepository.existsByEmail(userDTO.getEmail())) {
                    return ResponseDTO.error("Email already registered");
                }
                
                if (!user.getPhone().equals(userDTO.getPhone()) && 
                    userRepository.existsByPhone(userDTO.getPhone())) {
                    return ResponseDTO.error("Phone number already registered");
                }
                
                user.setName(userDTO.getName());
                user.setEmail(userDTO.getEmail());
                user.setPhone(userDTO.getPhone());
                user.setDateOfBirth(userDTO.getDateOfBirth());
                user.setAddress(userDTO.getAddress());
                user.setBloodGroup(userDTO.getBloodGroup());
                
                if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
                    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
                }
                
                User updatedUser = userRepository.save(user);
                return ResponseDTO.success("User updated successfully", convertToDTO(updatedUser));
            } else {
                return ResponseDTO.error("User not found or inactive");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error updating user: " + e.getMessage());
        }
    }
    
    public ResponseDTO deleteUser(Long id) {
        try {
            Optional<User> user = userRepository.findById(id);
            if (user.isPresent() && user.get().getIsActive()) {
                User existingUser = user.get();
                existingUser.setIsActive(false);
                userRepository.save(existingUser);
                return ResponseDTO.success("User deleted successfully");
            } else {
                return ResponseDTO.error("User not found or already inactive");
            }
        } catch (Exception e) {
            return ResponseDTO.error("Error deleting user: " + e.getMessage());
        }
    }
    
    public ResponseDTO searchUsersByName(String name) {
        try {
            List<User> users = userRepository.findByNameContainingIgnoreCase(name);
            List<UserDTO> userDTOs = users.stream()
                    .filter(User::getIsActive)
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());
            return ResponseDTO.success("Users retrieved successfully", userDTOs);
        } catch (Exception e) {
            return ResponseDTO.error("Error searching users: " + e.getMessage());
        }
    }
    
    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setDateOfBirth(user.getDateOfBirth());
        dto.setAddress(user.getAddress());
        dto.setBloodGroup(user.getBloodGroup());
        return dto;
    }
}