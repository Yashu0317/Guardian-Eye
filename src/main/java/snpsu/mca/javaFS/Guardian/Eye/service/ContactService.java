package snpsu.mca.javaFS.Guardian.Eye.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snpsu.mca.javaFS.Guardian.Eye.model.Contact;
import snpsu.mca.javaFS.Guardian.Eye.model.EmergencyAlert;
import snpsu.mca.javaFS.Guardian.Eye.repository.ContactRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    
    public Contact saveContact(Contact contact) {
        return contactRepository.save(contact);
    }
    
    public List<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
    
    public Optional<Contact> getContactById(Long id) {
        return contactRepository.findById(id);
    }
    
    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }
    
    public void sendEmergencyAlerts(EmergencyAlert alert) {
        // Implementation for sending emergency alerts to all contacts
        List<Contact> emergencyContacts = contactRepository.findByRelation("emergency");
        // Add your alert sending logic here (SMS, email, push notification, etc.)
        System.out.println("Sending emergency alert to contacts: " + emergencyContacts);
    }
}