package snpsu.mca.javaFS.Guardian.Eye.controller;

import snpsu.mca.javaFS.Guardian.Eye.dto.UserFamilyDTO;
import snpsu.mca.javaFS.Guardian.Eye.dto.ResponseDTO;
import snpsu.mca.javaFS.Guardian.Eye.service.UserFamilyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user-families")
@CrossOrigin(origins = "*")
public class UserFamilyController {
    
    private final UserFamilyService userFamilyService;
    
    @Autowired
    public UserFamilyController(UserFamilyService userFamilyService) {
        this.userFamilyService = userFamilyService;
    }
    
    @PostMapping
    public ResponseDTO createUserFamily(@RequestBody UserFamilyDTO userFamilyDTO) {
        return userFamilyService.createUserFamily(userFamilyDTO);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseDTO getUserFamiliesByUserId(@PathVariable Long userId) {
        return userFamilyService.getUserFamiliesByUserId(userId);
    }
    
    @GetMapping("/{id}")
    public ResponseDTO getUserFamilyById(@PathVariable Long id) {
        return userFamilyService.getUserFamilyById(id);
    }
    
    @PutMapping("/{id}")
    public ResponseDTO updateUserFamily(@PathVariable Long id, @RequestBody UserFamilyDTO userFamilyDTO) {
        return userFamilyService.updateUserFamily(id, userFamilyDTO);
    }
    
    @DeleteMapping("/{id}")
    public ResponseDTO deleteUserFamily(@PathVariable Long id) {
        return userFamilyService.deleteUserFamily(id);
    }
}