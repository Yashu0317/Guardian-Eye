package snpsu.mca.javaFS.Guardian.Eye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snpsu.mca.javaFS.Guardian.Eye.model.SafePlace;

import java.util.List;

public interface SafePlaceRepository extends JpaRepository<SafePlace, Long> {
    List<SafePlace> findByType(String type);
}