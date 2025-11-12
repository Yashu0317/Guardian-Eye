package snpsu.mca.javaFS.Guardian.Eye.controller;

import snpsu.mca.javaFS.Guardian.Eye.dto.UserDTO;
import snpsu.mca.javaFS.Guardian.Eye.dto.ResponseDTO;
import snpsu.mca.javaFS.Guardian.Eye.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping
    public ResponseDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }
    
    @GetMapping
    public ResponseDTO getAllUsers() {
        return userService.getAllUsers();
    }
    
    @GetMapping("/{id}")
    public ResponseDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }
    
    @PutMapping("/{id}")
    public ResponseDTO updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseDTO deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id);
    }
    
    @GetMapping("/search")
    public ResponseDTO searchUsersByName(@RequestParam String name) {
        return userService.searchUsersByName(name);
    }
}