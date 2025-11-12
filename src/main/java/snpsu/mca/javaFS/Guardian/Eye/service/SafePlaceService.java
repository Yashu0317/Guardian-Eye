package snpsu.mca.javaFS.Guardian.Eye.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import snpsu.mca.javaFS.Guardian.Eye.model.SafePlace;
import snpsu.mca.javaFS.Guardian.Eye.repository.SafePlaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class SafePlaceService {
    
    @Autowired
    private SafePlaceRepository safePlaceRepository;
    
    public SafePlace saveSafePlace(SafePlace safePlace) {
        return safePlaceRepository.save(safePlace);
    }
    
    public List<SafePlace> getAllSafePlaces() {
        return safePlaceRepository.findAll();
    }
    
    public Optional<SafePlace> getSafePlaceById(Long id) {
        return safePlaceRepository.findById(id);
    }
    
    public void deleteSafePlace(Long id) {
        safePlaceRepository.deleteById(id);
    }
    
    public List<SafePlace> getSafePlacesByType(String type) {
        return safePlaceRepository.findByType(type);
    }
}