package snpsu.mca.javaFS.Guardian.Eye.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import snpsu.mca.javaFS.Guardian.Eye.model.Contact;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {
    List<Contact> findByRelation(String relation);
}