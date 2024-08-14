-- Flyway migration script
-- V1__Initial_setup.sql
CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    password VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    user_role VARCHAR(20) NOT NULL
);

CREATE TABLE item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    item_type VARCHAR(20) NOT NULL,
    description TEXT,
);

CREATE TABLE reservation (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    item_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    start_time TIMESTAMP NOT NULL,
    end_time TIMESTAMP NOT NULL,
    reservation_status VARCHAR(20) NOT NULL,
    FOREIGN KEY (item_id) REFERENCES items(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert 3 users into the user table
INSERT INTO user (username, first_name, last_name, password, email, user_role)
VALUES
('admin', 'admin', 'admin', '$2a$10$6TfZM7vhksUg4ovjYi4ee.cJG6q.UHIw.rE42cQlX3Q2LRzwOmjO6', 'admin@hotmail.com', 'ADMIN'),
('teacher', 'teacher', 'teacher', '$10$ZcNSWprfq6Z16byjogD6/eeAz3Sbb1r8X3jWv7xwQjaCJYl7WQmMq', 'teacher@hotmail.com', 'TEACHER'),
('student', 'student', 'student', '$10$XIdqMlqdZKhij8k8lF1EbOXCHW2UcMkfcggZJIajtqeMc98mDNe/i', 'student@hotmail.com', 'STUDENT');