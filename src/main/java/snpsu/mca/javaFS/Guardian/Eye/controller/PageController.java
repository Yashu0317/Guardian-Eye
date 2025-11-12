package snpsu.mca.javaFS.Guardian.Eye.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import snpsu.mca.javaFS.Guardian.Eye.model.Contact;
import snpsu.mca.javaFS.Guardian.Eye.model.SafePlace;
import snpsu.mca.javaFS.Guardian.Eye.model.EmergencyAlert;
import snpsu.mca.javaFS.Guardian.Eye.service.ContactService;
import snpsu.mca.javaFS.Guardian.Eye.service.SafePlaceService;

import java.util.List;

@Controller
public class PageController {
    
    @Autowired
    private ContactService contactService;
    
    @Autowired
    private SafePlaceService safePlaceService;
    
    @GetMapping("/")
    public String home() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Add any data you want to display on dashboard
        model.addAttribute("message", "Welcome to Guardian Eye Dashboard!");
        
        // Add contacts and safe places to the model for initial display
        model.addAttribute("contacts", contactService.getAllContacts());
        model.addAttribute("safePlaces", safePlaceService.getAllSafePlaces());
        
        return "dashboard";
    }
    
    @GetMapping("/family-login")
    public String familyLogin() {
        return "family-login";
    }
    
    @GetMapping("/family-signup")
    public String familySignup() {
        return "family-signup";
    }
    
    // Contact CRUD endpoints
    @PostMapping("/dashboard/contacts")
    @ResponseBody
    public ResponseEntity<?> addContact(@RequestBody Contact contact) {
        Contact savedContact = contactService.saveContact(contact);
        return ResponseEntity.ok(savedContact);
    }
    
    @GetMapping("/dashboard/contacts")
    @ResponseBody
    public List<Contact> getContacts() {
        return contactService.getAllContacts();
    }
    
    @DeleteMapping("/dashboard/contacts/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
    
    // Safe Places CRUD endpoints
    @PostMapping("/dashboard/safe-places")
    @ResponseBody
    public ResponseEntity<?> addSafePlace(@RequestBody SafePlace safePlace) {
        SafePlace savedPlace = safePlaceService.saveSafePlace(safePlace);
        return ResponseEntity.ok(savedPlace);
    }
    
    @GetMapping("/dashboard/safe-places")
    @ResponseBody
    public List<SafePlace> getSafePlaces() {
        return safePlaceService.getAllSafePlaces();
    }
    
    @DeleteMapping("/dashboard/safe-places/{id}")
    @ResponseBody
    public ResponseEntity<?> deleteSafePlace(@PathVariable Long id) {
        safePlaceService.deleteSafePlace(id);
        return ResponseEntity.ok().build();
    }
    
    // Emergency alert endpoint
    @PostMapping("/dashboard/emergency-alert")
    @ResponseBody
    public ResponseEntity<?> sendEmergencyAlert(@RequestBody EmergencyAlert alert) {
        // Send alerts to all emergency contacts
        contactService.sendEmergencyAlerts(alert);
        return ResponseEntity.ok("Emergency alerts sent successfully");
    }
    
    // Additional feature pages
    @GetMapping("/emergency")
    public String emergencyPage(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "emergency";
    }
    
    @GetMapping("/location")
    public String locationPage() {
        return "location";
    }
    
    @GetMapping("/contacts")
    public String contactsPage(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "contacts";
    }
    
    @GetMapping("/family")
    public String familyPage() {
        return "family";
    }
    
 // Add this method to your PageController class
    @GetMapping("/userfamily")
    public String userFamilyPage() {
        return "userfamily";
    }
    
    @GetMapping("/quick-dial")
    public String quickDialPage(Model model) {
        model.addAttribute("contacts", contactService.getAllContacts());
        return "quick-dial";
    }
    
    @GetMapping("/safe-places")
    public String safePlacesPage(Model model) {
        model.addAttribute("safePlaces", safePlaceService.getAllSafePlaces());
        return "safe-places";
    }
    
    @GetMapping("/smart-alerts")
    public String smartAlertsPage() {
        return "smart-alerts";
    }
}