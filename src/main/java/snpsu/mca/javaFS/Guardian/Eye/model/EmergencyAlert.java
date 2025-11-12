package snpsu.mca.javaFS.Guardian.Eye.model;

public class EmergencyAlert {
    private Long userId;
    private String location;
    private String message;
    private String timestamp;
    
    // Constructors, Getters and Setters
    public EmergencyAlert() {}
    
    public EmergencyAlert(Long userId, String location, String message, String timestamp) {
        this.userId = userId;
        this.location = location;
        this.message = message;
        this.timestamp = timestamp;
    }
    
    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}