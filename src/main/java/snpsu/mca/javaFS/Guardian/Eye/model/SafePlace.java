package snpsu.mca.javaFS.Guardian.Eye.model;

import jakarta.persistence.*;

@Entity
@Table(name = "safe_places")
public class SafePlace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String address;
    
    @Column(nullable = false)
    private String type; // police, hospital, safehouse, public
    
    // Constructors
    public SafePlace() {}
    
    public SafePlace(String name, String address, String type) {
        this.name = name;
        this.address = address;
        this.type = type;
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}