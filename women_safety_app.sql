CREATE DATABASE women_safety_app;

USE women_safety_app;

-- Users table

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15) NOT NULL,
    password VARCHAR(255) NOT NULL,
    date_of_birth DATE,
    address TEXT,
    blood_group VARCHAR(5),
    is_active BOOLEAN DEFAULT TRUE,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
INSERT INTO users (name, email, phone, password, date_of_birth, address, blood_group, is_active) 
VALUES (
    'Priya Sharma', 
    'priya.sharma@example.com', 
    '+919876543210', 
    '$2a$10$hashedpassword123', 
    '1995-08-15', 
    '123 Main Street, Mumbai, Maharashtra - 400001', 
    'O+', 
    TRUE
);
-- Create test user with proper BCrypt password
INSERT INTO users (name, email, phone, password, date_of_birth, address, blood_group, is_active) 
VALUES (
    'Test User', 
    'test@example.com', 
    '+919876543210', 
    '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 
    '1990-01-01', 
    'Test Address', 
    'O+', 
    TRUE
);
select * from users;
-- Create index for better performance on email queries
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_is_active ON users(is_active);

-- Continue with your existing database
USE women_safety_app;

-- Emergency Contacts Table (Extended from your emergency_contact fields)
CREATE TABLE IF NOT EXISTS emergency_contacts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    email VARCHAR(100),
    relation ENUM('FAMILY', 'FRIEND', 'COLLEAGUE', 'EMERGENCY_SERVICE', 'OTHER') NOT NULL,
    priority_level INT DEFAULT 1,
    is_primary BOOLEAN DEFAULT FALSE,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active)
);
-- Insert sample emergency contacts
INSERT INTO emergency_contacts (user_id, name, phone, email, relation, priority_level, is_primary) VALUES
(1, 'Priya', '+919874563210', 'priya@example.com', 'FAMILY', 1, TRUE);
-- Safe Places Table
CREATE TABLE IF NOT EXISTS safe_places (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(100) NOT NULL,
    address TEXT NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    type ENUM('POLICE_STATION', 'HOSPITAL', 'SAFE_HOUSE', 'PUBLIC_PLACE', 'FRIEND_HOUSE', 'OTHER') NOT NULL,
    contact_phone VARCHAR(20),
    notes TEXT,
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_active (is_active)
);

-- Emergency Alerts Table
CREATE TABLE IF NOT EXISTS emergency_alerts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    alert_type ENUM('EMERGENCY', 'SOS', 'LOCATION_SHARE', 'AUTO_ALERT') NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    location_address TEXT,
    message TEXT,
    status ENUM('ACTIVE', 'RESOLVED', 'CANCELLED') DEFAULT 'ACTIVE',
    triggered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    resolved_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_triggered_at (triggered_at)
);

-- Alert Recipients Table (Many-to-Many relationship between alerts and contacts)
CREATE TABLE IF NOT EXISTS alert_recipients (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    alert_id BIGINT NOT NULL,
    contact_id BIGINT NOT NULL,
    notification_sent BOOLEAN DEFAULT FALSE,
    sent_at TIMESTAMP NULL,
    delivery_status ENUM('PENDING', 'SENT', 'DELIVERED', 'FAILED') DEFAULT 'PENDING',
    response_received BOOLEAN DEFAULT FALSE,
    response_text TEXT,
    responded_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (alert_id) REFERENCES emergency_alerts(id) ON DELETE CASCADE,
    FOREIGN KEY (contact_id) REFERENCES emergency_contacts(id) ON DELETE CASCADE,
    INDEX idx_alert_id (alert_id),
    INDEX idx_contact_id (contact_id),
    INDEX idx_delivery_status (delivery_status)
);

-- User Locations Table (for tracking and location sharing)
CREATE TABLE IF NOT EXISTS user_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    latitude DECIMAL(10, 8) NOT NULL,
    longitude DECIMAL(11, 8) NOT NULL,
    accuracy DECIMAL(8, 2),
    altitude DECIMAL(8, 2),
    speed DECIMAL(6, 2),
    heading DECIMAL(5, 2),
    location_address TEXT,
    is_shared BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at),
    INDEX idx_is_shared (is_shared)
);

-- Safety Check-ins Table
CREATE TABLE IF NOT EXISTS safety_checkins (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    checkin_type ENUM('MANUAL', 'AUTO', 'SCHEDULED') NOT NULL,
    latitude DECIMAL(10, 8),
    longitude DECIMAL(11, 8),
    location_address TEXT,
    status ENUM('SAFE', 'NEEDS_CHECK', 'OVERDUE') DEFAULT 'SAFE',
    checkin_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    next_checkin_time TIMESTAMP NULL,
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_checkin_time (checkin_time),
    INDEX idx_status (status)
);

-- Smart Alerts Configuration Table
CREATE TABLE IF NOT EXISTS smart_alerts_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL,
    alert_name VARCHAR(100) NOT NULL,
    alert_type ENUM('TIME_BASED', 'LOCATION_BASED', 'MOTION_BASED', 'SCHEDULE_BASED') NOT NULL,
    is_active BOOLEAN DEFAULT TRUE,
    config_data JSON,
    start_time TIME NULL,
    end_time TIME NULL,
    days_of_week VARCHAR(20), -- '1,2,3,4,5,6,7' for days
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_user_id (user_id),
    INDEX idx_is_active (is_active)
);

-- Audit Log Table
CREATE TABLE IF NOT EXISTS audit_logs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NULL,
    action_type VARCHAR(50) NOT NULL,
    table_name VARCHAR(50) NOT NULL,
    record_id BIGINT NULL,
    old_values JSON,
    new_values JSON,
    ip_address VARCHAR(45),
    user_agent TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_action_type (action_type),
    INDEX idx_created_at (created_at)
);



select * from emergency_contacts;