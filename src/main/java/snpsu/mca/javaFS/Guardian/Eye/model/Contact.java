package snpsu.mca.javaFS.Guardian.Eye.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phone;
    
    @Column(nullable = false)
    private String relation; // family, friend, colleague, emergency
    
    // Constructors
    public Contact() {}
    
    public Contact(String name, String phone, String relation) {
        this.name = name;
        this.phone = phone;
        this.relation = relation;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getRelation() { return relation; }
    public void setRelation(String relation) { this.relation = relation; }
}