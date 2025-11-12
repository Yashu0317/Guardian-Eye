package snpsu.mca.javaFS.Guardian.Eye.repository;

import snpsu.mca.javaFS.Guardian.Eye.entity.UserFamily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFamilyRepository extends JpaRepository<UserFamily, Long> {
    List<UserFamily> findByUserId(Long userId);
    List<UserFamily> findByUserIdAndIsEmergencyContactTrue(Long userId);
}